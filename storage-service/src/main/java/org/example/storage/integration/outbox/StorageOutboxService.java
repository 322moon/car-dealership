package org.example.storage.integration.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.events.DealershipEvent;
import org.example.events.EventType;
import org.example.events.OrderType;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageOutboxService {
    private final OutboxEventRepository repository;
    private final ObjectMapper objectMapper;

    public void saveOrderApproved(UUID orderId, OrderType orderType, String traceId, UUID assemblyOrderId) {
        saveEvent(EventType.ORDER_APPROVED, orderId, orderType, traceId, Map.of(
                "assemblyOrderId", assemblyOrderId.toString()
        ));
    }

    public void saveOrderRejected(UUID orderId, OrderType orderType, String traceId, String reason) {
        saveEvent(EventType.ORDER_REJECTED, orderId, orderType, traceId, Map.of(
                "reason", reason
        ));
    }

    private void saveEvent(EventType eventType, UUID orderId, OrderType orderType, String traceId, Map<String, Object> payload) {
        DealershipEvent event = new DealershipEvent(
                UUID.randomUUID(),
                eventType,
                orderId,
                orderType,
                traceId,
                Instant.now(),
                payload
        );

        OutboxEventJpaEntity entity = new OutboxEventJpaEntity();
        entity.setEventType(event.eventType().name());
        entity.setAggregateType("ASSEMBLY_ORDER");
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
