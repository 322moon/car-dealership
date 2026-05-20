package org.example.integration.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.events.DealershipEvent;
import org.example.events.EventType;
import org.example.events.OrderType;
import org.example.lab1.Domain.Entities.Cars.ConfiguredModel;
import org.example.lab1.Domain.Entities.Sales.ConfiguredCarOrder;
import org.example.lab1.Domain.Entities.Sales.OrderInStock;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderOutboxService {
    private final OutboxEventRepository repository;
    private final ObjectMapper objectMapper;

    public void saveOrderSentForApproval(OrderInStock order) {
        Map<String, Object> payload = Map.of(
                "carId", order.getCarId().toString()
        );

        saveEvent(order.getId(), OrderType.IN_STOCK, payload);
    }

    public void saveOrderSentForApproval(ConfiguredCarOrder order) {
        ConfiguredModel configuredModel = order.getConfiguredModel();
        Map<String, Object> payload = Map.of(
                "modelId", configuredModel.getModel().getId().toString(),
                "detailIds", List.of(
                        configuredModel.getTransmission().getId().toString(),
                        configuredModel.getWheels().getId().toString(),
                        configuredModel.getSteeringWheel().getId().toString(),
                        configuredModel.getInterior().getId().toString()
                )
        );

        saveEvent(order.getId(), OrderType.CONFIGURED, payload);
    }

    private void saveEvent(UUID orderId, OrderType orderType, Map<String, Object> payload) {
        String traceId = UUID.randomUUID().toString();
        DealershipEvent event = new DealershipEvent(
                UUID.randomUUID(),
                EventType.ORDER_SENT_FOR_APPROVAL,
                orderId,
                orderType,
                traceId,
                Instant.now(),
                payload
        );

        OutboxEventJpaEntity entity = new OutboxEventJpaEntity();
        entity.setEventType(event.eventType().name());
        entity.setAggregateType("ORDER");
        entity.setAggregateId(orderId);
        entity.setTraceId(traceId);
        entity.setPayload(toJson(event));
        entity.setStatus(OutboxStatus.NEW);
        repository.save(entity);
    }

    private String toJson(DealershipEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Cannot serialize outbox event", exception);
        }
    }
}
