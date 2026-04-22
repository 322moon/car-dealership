package org.example.Infrastructure.utils;

import org.example.Infrastructure.dto.model.ModelRequestDto;
import org.example.Infrastructure.dto.model.ModelResponseDto;
import org.example.lab1.Domain.Entities.Cars.Model;
import org.example.lab1.Domain.Entities.Details.Engine;
import org.example.lab1.Domain.Entities.Details.Interior;
import org.example.lab1.Domain.Entities.Details.SteeringWheel;
import org.example.lab1.Domain.Entities.Details.Transmission;
import org.example.lab1.Domain.Entities.Details.Wheels;
import org.example.lab1.Domain.ValueObjects.Price;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ModelDtoMapper {
    default ModelResponseDto toResponseDto(Model model) {
        if (model == null) {
            return null;
        }

        ModelResponseDto dto = new ModelResponseDto();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setBasePrice(model.getBasePrice().value());
        dto.setCarType(model.getCarType().name());
        dto.setCarBrand(model.getCarBrand().name());
        dto.setFuelType(model.getFuelType().name());
        dto.setDriveType(model.getDriveType().name());
        dto.setEngineCapacity(model.getEngine().getEngineCapacity());
        dto.setEnginePower(model.getEngine().getEnginePower());
        dto.setTransmissionId(model.getTransmission().getId());
        dto.setTransmissionName(model.getTransmission().getName());
        dto.setWheelsId(model.getWheels().getId());
        dto.setWheelsName(model.getWheels().getName());
        dto.setSteeringWheelId(model.getSteeringWheel().getId());
        dto.setSteeringWheelName(model.getSteeringWheel().getName());
        dto.setInteriorId(model.getInterior().getId());
        dto.setInteriorName(model.getInterior().getName());
        return dto;
    }

    default List<ModelResponseDto> toResponseDtoList(List<Model> models) {
        return models.stream()
                .map(this::toResponseDto)
                .toList();
    }

    default Model toNewDomain(
            ModelRequestDto requestDto,
            Transmission transmission,
            Wheels wheels,
            SteeringWheel steeringWheel,
            Interior interior
    ) {
        return toExistingDomain(UUID.randomUUID(), requestDto, transmission, wheels, steeringWheel, interior);
    }

    default Model toExistingDomain(
            UUID id,
            ModelRequestDto requestDto,
            Transmission transmission,
            Wheels wheels,
            SteeringWheel steeringWheel,
            Interior interior
    ) {
        return Model.builder()
                .id(id)
                .name(requestDto.getName())
                .basePrice(new Price(requestDto.getBasePrice()))
                .carType(requestDto.getCarType())
                .carBrand(requestDto.getCarBrand())
                .fuelType(requestDto.getFuelType())
                .driveType(requestDto.getDriveType())
                .engine(new Engine(new Price(requestDto.getEnginePrice()), requestDto.isEngineInStock(), requestDto.getEngineCapacity(), requestDto.getEnginePower()))
                .transmission(transmission)
                .wheels(wheels)
                .steeringWheel(steeringWheel)
                .interior(interior)
                .build();
    }
}
