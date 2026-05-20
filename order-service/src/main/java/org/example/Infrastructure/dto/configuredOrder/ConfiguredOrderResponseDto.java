package org.example.Infrastructure.dto.configuredOrder;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ConfiguredOrderResponseDto {
    private UUID id;
    private UUID customerId;
    private UUID managerId;
    private UUID modelId;
    private String modelName;
    private BigDecimal totalPrice;
    private String status;
    private UUID transmissionId;
    private String transmissionName;
    private UUID wheelsId;
    private String wheelsName;
    private UUID steeringWheelId;
    private String steeringWheelName;
    private UUID interiorId;
    private String interiorName;
}
