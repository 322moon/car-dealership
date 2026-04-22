package org.example.Infrastructure.dto.car;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.CarColor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CarRequestDto {
    @NotNull
    private UUID modelId;

    @NotNull
    private CarColor color;

    @NotNull
    @PositiveOrZero
    private BigDecimal price;

    private boolean inStock;

    private boolean availableForTestDrive;
}
