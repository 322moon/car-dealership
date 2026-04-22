package org.example.Infrastructure.utils;

import org.example.Infrastructure.dto.car.CarRequestDto;
import org.example.Infrastructure.dto.car.CarResponseDto;
import org.example.lab1.Domain.Entities.Cars.Car;
import org.example.lab1.Domain.Entities.Cars.Model;
import org.example.lab1.Domain.ValueObjects.Price;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CarDtoMapper {
    @Mapping(target = "modelId", source = "model.id")
    @Mapping(target = "modelName", source = "model.name")
    @Mapping(target = "brand", source = "model.carBrand")
    @Mapping(target = "price", source = "price.value")
    CarResponseDto toResponseDto(Car car);

    List<CarResponseDto> toResponseDtoList(List<Car> cars);

    default Car toNewDomain(CarRequestDto requestDto, Model model) {
        if (requestDto == null || model == null) {
            return null;
        }

        return Car.create(
                model,
                requestDto.getColor(),
                new Price(requestDto.getPrice()),
                requestDto.isInStock(),
                requestDto.isAvailableForTestDrive()
        );
    }

    default Car toExistingDomain(UUID id, CarRequestDto requestDto, Model model) {
        if (id == null || requestDto == null || model == null) {
            return null;
        }

        return Car.builder()
                .id(id)
                .model(model)
                .color(requestDto.getColor())
                .price(new Price(requestDto.getPrice()))
                .inStock(requestDto.isInStock())
                .isAvailableForTestDrive(requestDto.isAvailableForTestDrive())
                .build();
    }
}
