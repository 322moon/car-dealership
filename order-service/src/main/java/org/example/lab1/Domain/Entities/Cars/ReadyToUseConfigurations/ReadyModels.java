package org.example.lab1.Domain.Entities.Cars.ReadyToUseConfigurations;

import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.CarBrand;
import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.CarType;
import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.DriveType;
import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.FuelType;
import org.example.lab1.Domain.Entities.Cars.Model;
import org.example.lab1.Domain.Entities.Details.Engine;
import org.example.lab1.Domain.ValueObjects.Price;

import java.util.List;
import java.util.UUID;

public class ReadyModels {

    public static final UUID BMW_320i_id = UUID.nameUUIDFromBytes("320i".getBytes());
    public static final UUID BMW_330i_id = UUID.nameUUIDFromBytes("330i".getBytes());
    public static final UUID BMW_340i_id = UUID.nameUUIDFromBytes("340i".getBytes());

    private ReadyModels() {
    }

    public static List<Model> bmw3Series() {
        Model bmw320i = Model.builder()
                .id(BMW_320i_id)
                .name("BMW 320i")
                .basePrice(Price.of(3_200_000))
                .carType(CarType.SEDAN)
                .carBrand(CarBrand.BMW)
                .fuelType(FuelType.GASOLINE)
                .driveType(DriveType.RWD)
                .engine(new Engine(Price.of(250_000), true, 2.0, 184))
                .wheels(ReadyOptions.findWheelsByName("17'' Standard"))
                .transmission(ReadyOptions.findTransmissionByName("Automatic 8AT"))
                .steeringWheel(ReadyOptions.findSteeringWheelByName("Sport Leather Standard"))
                .interior(ReadyOptions.findInteriorByName("Fabric Graphite"))
                .build();

        Model bmw330i = Model.builder()
                .id(BMW_330i_id)
                .name("BMW 330i")
                .basePrice(Price.of(3_850_000))
                .carType(CarType.SEDAN)
                .carBrand(CarBrand.BMW)
                .fuelType(FuelType.GASOLINE)
                .driveType(DriveType.RWD)
                .engine(new Engine(Price.of(320_000), true, 2.0, 258))
                .wheels(ReadyOptions.findWheelsByName("18'' Aero"))
                .transmission(ReadyOptions.findTransmissionByName("Automatic 8AT"))
                .steeringWheel(ReadyOptions.findSteeringWheelByName("M-Sport Heated"))
                .interior(ReadyOptions.findInteriorByName("Leather Dakota"))
                .build();

        Model bmwM340i = Model.builder()
                .id(BMW_340i_id)
                .name("BMW M340i")
                .basePrice(Price.of(4_700_000))
                .carType(CarType.SEDAN)
                .carBrand(CarBrand.BMW)
                .fuelType(FuelType.GASOLINE)
                .driveType(DriveType.AWD)
                .engine(new Engine(Price.of(450_000), true, 3.0, 387))
                .wheels(ReadyOptions.findWheelsByName("19'' M-Sport"))
                .transmission(ReadyOptions.findTransmissionByName("Automatic 8AT Sport"))
                .steeringWheel(ReadyOptions.findSteeringWheelByName("M-Sport Heated"))
                .interior(ReadyOptions.findInteriorByName("Sport Performance"))
                .build();

        return List.of(bmw320i, bmw330i, bmwM340i);
    }

    public static Model bmw320i() {
        return findByName("BMW 320i");
    }

    public static Model bmw330i() {
        return findByName("BMW 330i");
    }

    public static Model bmwM340i() {
        return findByName("BMW M340i");
    }

    public static Model findByName(String modelName) {
        return bmw3Series().stream()
                .filter(model -> model.getName().equalsIgnoreCase(modelName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown ready model: " + modelName));
    }
}
