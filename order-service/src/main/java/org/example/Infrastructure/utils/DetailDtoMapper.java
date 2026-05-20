package org.example.Infrastructure.utils;

import org.example.Infrastructure.dto.detail.DetailRequestDto;
import org.example.Infrastructure.dto.detail.DetailResponseDto;
import org.example.lab1.Domain.Entities.Details.Detail;
import org.example.lab1.Domain.Entities.Details.DetailType;
import org.example.lab1.Domain.Entities.Details.Interior;
import org.example.lab1.Domain.Entities.Details.SteeringWheel;
import org.example.lab1.Domain.Entities.Details.Transmission;
import org.example.lab1.Domain.Entities.Details.Wheels;
import org.example.lab1.Domain.ValueObjects.Price;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface DetailDtoMapper {
    default DetailResponseDto toResponseDto(Detail detail) {
        if (detail == null) {
            return null;
        }

        DetailResponseDto dto = new DetailResponseDto();
        dto.setId(detail.getId());
        dto.setName(detail.getName());
        dto.setType(detail.getType().name());
        dto.setCompatibleModelIds(detail.getCompatibleModels());
        dto.setInStock(detail.isInStock());
        dto.setPrice(detail.getPrice().value());
        if (detail instanceof Transmission transmission) {
            dto.setTransmissionType(transmission.getTransmissionType().name());
        }
        return dto;
    }

    default List<DetailResponseDto> toResponseDtoList(List<Detail> details) {
        return details.stream()
                .map(this::toResponseDto)
                .toList();
    }

    default Detail toNewDomain(DetailRequestDto requestDto) {
        return toExistingDomain(UUID.randomUUID(), requestDto);
    }

    default Detail toExistingDomain(UUID id, DetailRequestDto requestDto) {
        List<UUID> compatibleModelIds = requestDto.getCompatibleModelIds() == null
                ? List.of()
                : requestDto.getCompatibleModelIds();

        if (requestDto.getType() == DetailType.TRANSMISSION) {
            return new Transmission(
                    id,
                    requestDto.getName(),
                    requestDto.getTransmissionType(),
                    compatibleModelIds,
                    requestDto.isInStock(),
                    new Price(requestDto.getPrice())
            );
        }
        if (requestDto.getType() == DetailType.WHEELS) {
            return new Wheels(id, requestDto.getName(), compatibleModelIds, requestDto.isInStock(), new Price(requestDto.getPrice()));
        }
        if (requestDto.getType() == DetailType.STEERING_WHEEL) {
            return new SteeringWheel(id, requestDto.getName(), compatibleModelIds, requestDto.isInStock(), new Price(requestDto.getPrice()));
        }
        if (requestDto.getType() == DetailType.INTERIOR) {
            return new Interior(id, requestDto.getName(), compatibleModelIds, requestDto.isInStock(), new Price(requestDto.getPrice()));
        }

        throw new IllegalArgumentException("Engine is not supported by Detail DTO because domain Engine is not a Detail subclass");
    }
}
