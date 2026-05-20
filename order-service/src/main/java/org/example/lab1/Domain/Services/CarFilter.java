package org.example.lab1.Domain.Services;

import lombok.Builder;
import lombok.Getter;
import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.*;

import java.math.BigDecimal;

@Getter
@Builder
public class CarFilter {
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private CarBrand brand;
    private String modelName;
    private CarType carType;
    private FuelType fuelType;
    private Integer minEnginePower;
    private Integer maxEnginePower;
    private Double minEngineCapacity;
    private Double maxEngineCapacity;
    private TransmissionType transmissionType;
    private DriveType driveType;
    private CarColor color;
}
