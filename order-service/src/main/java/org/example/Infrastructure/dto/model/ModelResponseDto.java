package org.example.Infrastructure.dto.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ModelResponseDto {
    private UUID id;
    private String name;
    private BigDecimal basePrice;
    private String carType;
    private String carBrand;
    private String fuelType;
    private String driveType;
    private double engineCapacity;
    private int enginePower;
    private UUID transmissionId;
    private String transmissionName;
    private UUID wheelsId;
    private String wheelsName;
    private UUID steeringWheelId;
    private String steeringWheelName;
    private UUID interiorId;
    private String interiorName;
}
