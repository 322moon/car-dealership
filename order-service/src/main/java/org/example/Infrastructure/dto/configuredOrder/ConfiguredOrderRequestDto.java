package org.example.Infrastructure.dto.configuredOrder;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ConfiguredOrderRequestDto {
    @NotNull
    private UUID modelId;

    @NotNull
    @PositiveOrZero
    private BigDecimal totalPrice;

    @NotNull
    private UUID transmissionId;

    @NotNull
    private UUID wheelsId;

    @NotNull
    private UUID steeringWheelId;

    @NotNull
    private UUID interiorId;
}
