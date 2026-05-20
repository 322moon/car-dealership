package org.example.storage.grpc;

import java.math.BigDecimal;
import java.util.UUID;

public record AvailableCarView(
        UUID id,
        UUID modelId,
        String modelName,
        String brand,
        String color,
        BigDecimal price
) {
}
