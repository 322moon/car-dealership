package org.example.Infrastructure.dto.car;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CarResponseDto {
    private UUID id;
    private UUID modelId;
    private String modelName;
    private String brand;
    private String color;
    private BigDecimal price;
    private boolean inStock;
    private boolean availableForTestDrive;
}
