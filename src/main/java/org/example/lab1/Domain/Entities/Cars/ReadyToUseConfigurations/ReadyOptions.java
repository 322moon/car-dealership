package org.example.lab1.Domain.Entities.Cars.ReadyToUseConfigurations;

import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.TransmissionType;
import org.example.lab1.Domain.Entities.Cars.Model;
import org.example.lab1.Domain.Entities.Details.*;
import org.example.lab1.Domain.ValueObjects.Price;
import java.util.List;
import java.util.UUID;

public class ReadyOptions {

    private static List<Wheels> wheels = List.of(
            new Wheels(UUID.nameUUIDFromBytes("17Standard".getBytes()), "17'' Standard", List.of(ReadyModels.BMW_320i_id), true, Price.of(0)),
            new Wheels(UUID.nameUUIDFromBytes("18Aero".getBytes()), "18'' Aero", List.of(ReadyModels.BMW_320i_id, ReadyModels.BMW_330i_id), true, Price.of(45_000)),
            new Wheels(UUID.nameUUIDFromBytes("19MSport".getBytes()), "19'' M-Sport", List.of(ReadyModels.BMW_320i_id, ReadyModels.BMW_330i_id, ReadyModels.BMW_340i_id), true, Price.of(95_000))
    );

    private static List<Transmission> transmissions = List.of(
            new Transmission(UUID.nameUUIDFromBytes("Automatic8AT".getBytes()), "Automatic 8AT", TransmissionType.AT, List.of(ReadyModels.BMW_320i_id, ReadyModels.BMW_330i_id), true, Price.of(0)),
            new Transmission(UUID.nameUUIDFromBytes("Manual6MT".getBytes()), "Manual 6MT", TransmissionType.MT, List.of(ReadyModels.BMW_320i_id, ReadyModels.BMW_330i_id), true, Price.of(-30_000)),
            new Transmission(UUID.nameUUIDFromBytes("Automatic8ATSport".getBytes()), "Automatic 8AT Sport", TransmissionType.AT, List.of(ReadyModels.BMW_330i_id, ReadyModels.BMW_340i_id), true, Price.of(100_000))
    );

    private static List<SteeringWheel> steeringWheels = List.of(
            new SteeringWheel(UUID.nameUUIDFromBytes("SportLeatherStandard".getBytes()), "Sport Leather Standard", List.of(ReadyModels.BMW_320i_id, ReadyModels.BMW_330i_id), true, Price.of(0)),
            new SteeringWheel(UUID.nameUUIDFromBytes("MSportHeated".getBytes()), "M-Sport Heated", List.of(ReadyModels.BMW_320i_id, ReadyModels.BMW_330i_id, ReadyModels.BMW_340i_id), true, Price.of(25_000))
    );

    private static List<Interior> interiors = List.of(
            new Interior(UUID.nameUUIDFromBytes("FabricGraphite".getBytes()), "Fabric Graphite", List.of(ReadyModels.BMW_320i_id), true, Price.of(0)),
            new Interior(UUID.nameUUIDFromBytes("LeatherDakota".getBytes()), "Leather Dakota", List.of(ReadyModels.BMW_320i_id, ReadyModels.BMW_330i_id), true, Price.of(110_000)),
            new Interior(UUID.nameUUIDFromBytes("SportPerformance".getBytes()), "Sport Performance", List.of(ReadyModels.BMW_330i_id, ReadyModels.BMW_340i_id), true, Price.of(160_000))
    );

    private ReadyOptions(){
    }

    public static ModelOptions inStockOptions(){
        return new ModelOptions(wheels, transmissions, steeringWheels, interiors);
    }

    public static ModelOptions forModel(Model model){
        List<Wheels> compatibleWheels = wheels.stream()
                .filter(w -> w.isCompatibleWith(model))
                .toList();
        List<SteeringWheel> compatibleSteeringWheels = steeringWheels.stream()
                .filter(sw -> sw.isCompatibleWith(model))
                .toList();
        List<Transmission> compatibleTransmissions = transmissions.stream()
                .filter(t -> t.isCompatibleWith(model))
                .toList();
        List<Interior> compatibleInteriors = interiors.stream()
                .filter(i -> i.isCompatibleWith(model))
                .toList();

        return new ModelOptions(compatibleWheels, compatibleTransmissions, compatibleSteeringWheels, compatibleInteriors);
    }

    public static Interior findInteriorByName(String detailName){
        ModelOptions options = ReadyOptions.inStockOptions();
        return options.getInteriors().stream()
                .filter(option -> option.getName().equalsIgnoreCase(detailName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown interior model: " + detailName));
    }

    public static Wheels findWheelsByName(String detailName){
        ModelOptions options = ReadyOptions.inStockOptions();
        return options.getWheels().stream()
                .filter(option -> option.getName().equalsIgnoreCase(detailName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown wheels model: " + detailName));
    }

    public static Transmission findTransmissionByName(String detailName){
        ModelOptions options = ReadyOptions.inStockOptions();
        return options.getTransmissions().stream()
                .filter(option -> option.getName().equalsIgnoreCase(detailName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown transmission model: " + detailName));
    }

    public static SteeringWheel findSteeringWheelByName(String detailName){
        ModelOptions options = ReadyOptions.inStockOptions();
        return options.getSteeringWheels().stream()
                .filter(option -> option.getName().equalsIgnoreCase(detailName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown steering wheels model: " + detailName));
    }

//    public static ModelOptions findByCompatibility(Model model){
//
//        return switch (detailType){
//            case WHEELS -> options.getWheels().stream()
//                    .filter(d -> d.isCompatibleWith(model))
//                    .findFirst()
//                    .orElseThrow(() -> new DomainValidationException("Can't find compatible detail for: " + model.getName()));
//
//            case STEERING_WHEEL -> options.getSteeringWheels().stream()
//                    .filter(steeringWheel -> steeringWheel.isCompatibleWith(model))
//                    .findFirst()
//                    .orElseThrow(() -> new DomainValidationException("Can't find compatible detail for: " + model.getName()));
//
//            case TRANSMISSION -> options.getTransmissions().stream()
//                    .filter(transmission -> transmission.isCompatibleWith(model))
//                    .findFirst()
//                    .orElseThrow(() -> new DomainValidationException("Can't find compatible detail for: " + model.getName()));
//
//            case INTERIOR -> options.getInteriors().stream()
//                    .filter(interior -> interior.isCompatibleWith(model))
//                    .findFirst()
//                    .orElseThrow(() -> new DomainValidationException("Can't find compatible detail for: " + model.getName()));
//            default -> throw new DomainValidationException("Unknown detail type");
//        };
//    }
}
