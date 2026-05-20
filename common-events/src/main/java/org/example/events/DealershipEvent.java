package org.example.events;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record DealershipEvent(
        UUID eventId,
        EventType eventType,
        UUID orderId,
        OrderType orderType,
        String traceId,
        Instant occurredAt,
        Map<String, Object> payload
) {
}
