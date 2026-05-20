package org.example.Infrastructure.dto.availablecar;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class AvailableCarResponseDto {
    private UUID id;
    private UUID modelId;
    private String modelName;
    private String brand;
    private String color;
    private BigDecimal price;
}
