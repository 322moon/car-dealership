package org.example.integration.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.events.DealershipEvent;
import org.example.events.EventTopics;
import org.example.events.EventType;
import org.example.events.OrderType;
import org.example.integration.inbox.ProcessedMessageJpaEntity;
import org.example.integration.inbox.ProcessedMessageRepository;
import org.example.lab1.Domain.Entities.Sales.ConfiguredCarOrder;
import org.example.lab1.Domain.Entities.Sales.OrderInStock;
import org.example.lab1.Domain.Services.AdminService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class StorageEventsConsumer {
    private final ObjectMapper objectMapper;
    private final ProcessedMessageRepository processedMessageRepository;
    private final AdminService adminService;

    @KafkaListener(topics = EventTopics.STORAGE_EVENTS, groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    public void handle(String message) {
        DealershipEvent event = readEvent(message);
        if (processedMessageRepository.existsById(event.eventId())) {
            log.info("Skip duplicate storage event eventId={} traceId={}", event.eventId(), event.traceId());
            return;
        }

        log.info("Received storage event eventId={} eventType={} orderId={} traceId={}",
                event.eventId(), event.eventType(), event.orderId(), event.traceId());
        if (event.eventType() == EventType.ORDER_APPROVED) {
            approveOrder(event);
        } else if (event.eventType() == EventType.ORDER_REJECTED) {
            rejectOrder(event);
        } else {
            return;
        }

        ProcessedMessageJpaEntity processedMessage = new ProcessedMessageJpaEntity();
        processedMessage.setEventId(event.eventId());
        processedMessage.setEventType(event.eventType().name());
        processedMessageRepository.save(processedMessage);
    }

    private void approveOrder(DealershipEvent event) {
        if (event.orderType() == OrderType.IN_STOCK) {
            OrderInStock order = adminService.findInStockOrderById(event.orderId());
            order.markReadyForPickUp();
            adminService.saveInStockOrder(order);
            log.info("In-stock order marked ready for pickup orderId={} traceId={}", event.orderId(), event.traceId());
            return;
        }

        ConfiguredCarOrder order = adminService.findConfiguredOrderById(event.orderId());
        order.markAwaitingDelivery();
        order.markReadyForPickUp();
        adminService.saveConfiguredOrder(order);
        log.info("Configured order marked ready for pickup orderId={} traceId={}", event.orderId(), event.traceId());
    }

    private void rejectOrder(DealershipEvent event) {
        if (event.orderType() == OrderType.IN_STOCK) {
            OrderInStock order = adminService.findInStockOrderById(event.orderId());
            order.cancel();
            adminService.saveInStockOrder(order);
            log.info("In-stock order cancelled after storage rejection orderId={} traceId={}", event.orderId(), event.traceId());
            return;
        }

        ConfiguredCarOrder order = adminService.findConfiguredOrderById(event.orderId());
        order.cancel();
        adminService.saveConfiguredOrder(order);
        log.info("Configured order cancelled after storage rejection orderId={} traceId={}", event.orderId(), event.traceId());
    }

    private DealershipEvent readEvent(String message) {
        try {
            return objectMapper.readValue(message, DealershipEvent.class);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Cannot deserialize storage event", exception);
        }
    }
}
