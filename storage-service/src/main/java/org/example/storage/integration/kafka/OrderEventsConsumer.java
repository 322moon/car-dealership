package org.example.storage.integration.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.events.DealershipEvent;
import org.example.events.EventTopics;
import org.example.events.EventType;
import org.example.events.OrderType;
import org.example.storage.assembly.AssemblyOrderJpaEntity;
import org.example.storage.assembly.AssemblyOrderRepository;
import org.example.storage.assembly.AssemblyOrderStatus;
import org.example.storage.assembly.SourceOrderType;
import org.example.storage.integration.inbox.ProcessedMessageJpaEntity;
import org.example.storage.integration.inbox.ProcessedMessageRepository;
import org.example.storage.integration.outbox.StorageOutboxService;
import org.example.storage.stock.ReservationResult;
import org.example.storage.stock.StockReservationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventsConsumer {
    private final ObjectMapper objectMapper;
    private final ProcessedMessageRepository processedMessageRepository;
    private final AssemblyOrderRepository assemblyOrderRepository;
    private final StorageOutboxService storageOutboxService;
    private final StockReservationService stockReservationService;

    @KafkaListener(topics = EventTopics.ORDER_EVENTS, groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    public void handle(String message) {
        DealershipEvent event = readEvent(message);
        if (event.eventType() != EventType.ORDER_SENT_FOR_APPROVAL) {
            return;
        }
        if (processedMessageRepository.existsById(event.eventId())) {
            log.info("Skip duplicate order event eventId={} traceId={}", event.eventId(), event.traceId());
            return;
        }

        log.info("Received order event eventId={} orderId={} type={} traceId={}",
                event.eventId(), event.orderId(), event.orderType(), event.traceId());
        ReservationResult reservationResult = reserveStock(event);
        AssemblyOrderJpaEntity assemblyOrder = createAssemblyOrder(event, reservationResult);
        if (reservationResult.successful()) {
            storageOutboxService.saveOrderApproved(event.orderId(), event.orderType(), event.traceId(), assemblyOrder.getId());
            log.info("Assembly order approved assemblyOrderId={} orderId={} traceId={}",
                    assemblyOrder.getId(), event.orderId(), event.traceId());
        } else {
            storageOutboxService.saveOrderRejected(event.orderId(), event.orderType(), event.traceId(), reservationResult.failReason());
            log.info("Assembly order rejected assemblyOrderId={} orderId={} traceId={} reason={}",
                    assemblyOrder.getId(), event.orderId(), event.traceId(), reservationResult.failReason());
        }

        ProcessedMessageJpaEntity processedMessage = new ProcessedMessageJpaEntity();
        processedMessage.setEventId(event.eventId());
        processedMessage.setEventType(event.eventType().name());
        processedMessageRepository.save(processedMessage);
    }

    private ReservationResult reserveStock(DealershipEvent event) {
        if (event.orderType() == OrderType.IN_STOCK) {
            return stockReservationService.reserveCar(payloadUuid(event, "carId"));
        }
        return stockReservationService.reserveDetails(payloadUuidList(event, "detailIds"));
    }

    private AssemblyOrderJpaEntity createAssemblyOrder(DealershipEvent event, ReservationResult reservationResult) {
        AssemblyOrderJpaEntity assemblyOrder = new AssemblyOrderJpaEntity();
        assemblyOrder.setSourceOrderId(event.orderId());
        assemblyOrder.setSourceOrderType(SourceOrderType.valueOf(event.orderType().name()));
        if (event.payload().containsKey("carId")) {
            assemblyOrder.setCarId(payloadUuid(event, "carId"));
        }
        if (event.payload().containsKey("modelId")) {
            assemblyOrder.setModelId(payloadUuid(event, "modelId"));
        }
        assemblyOrder.setTraceId(event.traceId());
        assemblyOrder.setStatus(reservationResult.successful() ? AssemblyOrderStatus.ASSEMBLED : AssemblyOrderStatus.FAIL);
        assemblyOrder.setFailReason(reservationResult.failReason());
        assemblyOrder.setRemoved(false);
        return assemblyOrderRepository.save(assemblyOrder);
    }

    private UUID payloadUuid(DealershipEvent event, String fieldName) {
        Object value = event.payload().get(fieldName);
        if (value == null) {
            throw new IllegalArgumentException("Missing event payload field: " + fieldName);
        }
        return UUID.fromString(value.toString());
    }

    private List<UUID> payloadUuidList(DealershipEvent event, String fieldName) {
        Object value = event.payload().get(fieldName);
        if (!(value instanceof List<?> rawValues)) {
            throw new IllegalArgumentException("Missing event payload list field: " + fieldName);
        }
        return rawValues.stream()
                .map(Object::toString)
                .map(UUID::fromString)
                .toList();
    }

    private DealershipEvent readEvent(String message) {
        try {
            return objectMapper.readValue(message, DealershipEvent.class);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Cannot deserialize order event", exception);
        }
    }
}
