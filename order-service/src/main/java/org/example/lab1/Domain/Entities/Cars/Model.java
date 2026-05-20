package org.example.lab1.Domain.Entities.Cars;

import lombok.Builder;
import lombok.Getter;
import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.*;
import org.example.lab1.Domain.Entities.Details.*;
import org.example.lab1.Domain.ValueObjects.Price;

import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class Model {
    private UUID id;
    private String name;
    private Price basePrice;
    private CarType carType;
    private CarBrand carBrand;
    private FuelType fuelType;
    private DriveType driveType;
    private Engine engine;
    private Transmission transmission;
    private Wheels wheels;
    private SteeringWheel steeringWheel;
    private Interior interior;
}
