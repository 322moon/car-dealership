package org.example.persistence.utils;

import org.example.lab1.Domain.Entities.Cars.Car;
import org.example.lab1.Domain.Entities.Cars.Model;
import org.example.lab1.Domain.Entities.Details.Engine;
import org.example.lab1.Domain.Entities.Details.Interior;
import org.example.lab1.Domain.Entities.Details.SteeringWheel;
import org.example.lab1.Domain.Entities.Details.Transmission;
import org.example.lab1.Domain.Entities.Details.Wheels;
import org.example.lab1.Domain.ValueObjects.Price;
import org.example.persistence.entity.CarJpaEntity;
import org.example.persistence.entity.CarModelJpaEntity;
import org.example.persistence.entity.DetailJpaEntity;
import org.example.persistence.entity.EngineJpaEntity;
import org.example.persistence.entity.InteriorJpaEntity;
import org.example.persistence.entity.SteeringWheelJpaEntity;
import org.example.persistence.entity.TransmissionJpaEntity;
import org.example.persistence.entity.WheelsJpaEntity;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CarPersistenceMapper {

    default Car toDomain(CarJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return Car.builder()
                .id(entity.getId())
                .model(toDomain(entity.getModel()))
                .color(entity.getColor())
                .price(new Price(entity.getPrice()))
                .inStock(entity.isInStock())
                .isAvailableForTestDrive(entity.isAvailableForTestDrive())
                .build();
    }

    default Model toDomain(CarModelJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return Model.builder()
                .id(entity.getId())
                .name(entity.getName())
                .basePrice(new Price(entity.getBasePrice()))
                .carType(entity.getCarType())
                .carBrand(entity.getCarBrand())
                .fuelType(entity.getFuelType())
                .driveType(entity.getDriveType())
                .engine(toDomain(entity.getEngine()))
                .transmission(toDomain(entity.getTransmission()))
                .wheels(toDomain(entity.getWheels()))
                .steeringWheel(toDomain(entity.getSteeringWheel()))
                .interior(toDomain(entity.getInterior()))
                .build();
    }

    default org.example.lab1.Domain.Entities.Details.Detail detailToDomain(DetailJpaEntity entity) {
        if (entity instanceof TransmissionJpaEntity transmission) {
            return toDomain(transmission);
        }
        if (entity instanceof WheelsJpaEntity wheels) {
            return toDomain(wheels);
        }
        if (entity instanceof SteeringWheelJpaEntity steeringWheel) {
            return toDomain(steeringWheel);
        }
        if (entity instanceof InteriorJpaEntity interior) {
            return toDomain(interior);
        }

        throw new IllegalArgumentException("Unsupported detail entity type: " + entity.getClass().getSimpleName());
    }

    default Engine toDomain(EngineJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Engine(
                new Price(entity.getPrice()),
                entity.isInStock(),
                entity.getEngineCapacity(),
                entity.getEnginePower()
        );
    }

    default Transmission toDomain(TransmissionJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Transmission(
                entity.getId(),
                entity.getName(),
                entity.getTransmissionType(),
                compatibleModelIds(entity.getCompatibleModels()),
                entity.isInStock(),
                new Price(entity.getPrice())
        );
    }

    default Wheels toDomain(WheelsJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Wheels(
                entity.getId(),
                entity.getName(),
                compatibleModelIds(entity.getCompatibleModels()),
                entity.isInStock(),
                new Price(entity.getPrice())
        );
    }

    default SteeringWheel toDomain(SteeringWheelJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new SteeringWheel(
                entity.getId(),
                entity.getName(),
                compatibleModelIds(entity.getCompatibleModels()),
                entity.isInStock(),
                new Price(entity.getPrice())
        );
    }

    default Interior toDomain(InteriorJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Interior(
                entity.getId(),
                entity.getName(),
                compatibleModelIds(entity.getCompatibleModels()),
                entity.isInStock(),
                new Price(entity.getPrice())
        );
    }

    default List<UUID> compatibleModelIds(Set<CarModelJpaEntity> models) {
        if (models == null || models.isEmpty()) {
            return List.of();
        }

        return models.stream()
                .map(CarModelJpaEntity::getId)
                .toList();
    }
}
