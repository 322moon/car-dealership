package org.example.Infrastructure.utils;

import org.example.Infrastructure.dto.configuredOrder.ConfiguredOrderResponseDto;
import org.example.Infrastructure.dto.inStockOrder.InStockOrderResponseDto;
import org.example.lab1.Domain.Entities.Cars.ConfiguredModel;
import org.example.lab1.Domain.Entities.Sales.ConfiguredCarOrder;
import org.example.lab1.Domain.Entities.Sales.OrderInStock;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderDtoMapper {
    default InStockOrderResponseDto toResponseDto(OrderInStock order) {
        if (order == null) {
            return null;
        }

        InStockOrderResponseDto dto = new InStockOrderResponseDto();
        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomerId());
        dto.setCarId(order.getCarId());
        dto.setManagerId(order.getManagerId());
        dto.setTotalPrice(order.getTotalPrice().value());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setStatus(order.getStatus().name());
        return dto;
    }

    default ConfiguredOrderResponseDto toResponseDto(ConfiguredCarOrder order) {
        if (order == null) {
            return null;
        }

        ConfiguredOrderResponseDto dto = new ConfiguredOrderResponseDto();
        ConfiguredModel configuredModel = order.getConfiguredModel();
        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomerId());
        dto.setManagerId(order.getManagerId());
        dto.setModelId(configuredModel.getModel().getId());
        dto.setModelName(configuredModel.getModel().getName());
        dto.setTotalPrice(order.getTotalPrice().value());
        dto.setStatus(order.getStatus().name());
        dto.setTransmissionId(configuredModel.getTransmission().getId());
        dto.setTransmissionName(configuredModel.getTransmission().getName());
        dto.setWheelsId(configuredModel.getWheels().getId());
        dto.setWheelsName(configuredModel.getWheels().getName());
        dto.setSteeringWheelId(configuredModel.getSteeringWheel().getId());
        dto.setSteeringWheelName(configuredModel.getSteeringWheel().getName());
        dto.setInteriorId(configuredModel.getInterior().getId());
        dto.setInteriorName(configuredModel.getInterior().getName());
        return dto;
    }

    default List<InStockOrderResponseDto> toInStockResponseDtoList(List<OrderInStock> orders) {
        return orders.stream()
                .map(this::toResponseDto)
                .toList();
    }

    default List<ConfiguredOrderResponseDto> toConfiguredResponseDtoList(List<ConfiguredCarOrder> orders) {
        return orders.stream()
                .map(this::toResponseDto)
                .toList();
    }
}
