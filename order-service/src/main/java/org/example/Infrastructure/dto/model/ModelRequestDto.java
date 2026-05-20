package org.example.Infrastructure.dto.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.CarBrand;
import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.CarType;
import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.DriveType;
import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.FuelType;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ModelRequestDto {
    @NotNull
    private String name;

    @NotNull
    @PositiveOrZero
    private BigDecimal basePrice;

    @NotNull
    private CarType carType;

    @NotNull
    private CarBrand carBrand;

    @NotNull
    private FuelType fuelType;

    @NotNull
    private DriveType driveType;

    @NotNull
    @PositiveOrZero
    private BigDecimal enginePrice;

    private boolean engineInStock;

    private double engineCapacity;

    private int enginePower;

    @NotNull
    private UUID transmissionId;

    @NotNull
    private UUID wheelsId;

    @NotNull
    private UUID steeringWheelId;

    @NotNull
    private UUID interiorId;
}
