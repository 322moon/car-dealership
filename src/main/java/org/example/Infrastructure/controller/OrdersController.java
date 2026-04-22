package org.example.Infrastructure.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.Infrastructure.dto.configuredOrder.ConfiguredOrderRequestDto;
import org.example.Infrastructure.dto.configuredOrder.ConfiguredOrderResponseDto;
import org.example.Infrastructure.dto.inStockOrder.InStockOrderRequestDto;
import org.example.Infrastructure.dto.inStockOrder.InStockOrderResponseDto;
import org.example.Infrastructure.utils.OrderDtoMapper;
import org.example.exception.DomainValidationExceptions.DomainValidationException;
import org.example.lab1.Domain.Entities.Cars.ConfiguredModel;
import org.example.lab1.Domain.Entities.Details.Detail;
import org.example.lab1.Domain.Entities.Details.Interior;
import org.example.lab1.Domain.Entities.Details.SteeringWheel;
import org.example.lab1.Domain.Entities.Details.Transmission;
import org.example.lab1.Domain.Entities.Details.Wheels;
import org.example.lab1.Domain.configurator.Configurator;
import org.example.lab1.Domain.Services.AdminService;
import org.example.lab1.Domain.Services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrdersController {
    private final OrderService orderService;
    private final AdminService adminService;
    private final OrderDtoMapper orderDtoMapper;

    @PostMapping("/in-stock")
    @ResponseStatus(HttpStatus.CREATED)
    public InStockOrderResponseDto createInStockOrder(@Valid @RequestBody InStockOrderRequestDto requestDto) {
        return orderDtoMapper.toResponseDto(orderService.createOrderInStock(
                requestDto.getCustomerId(),
                requestDto.getCarId()
        ));
    }

    @GetMapping("/in-stock")
    public List<InStockOrderResponseDto> findAllInStockOrders() {
        return orderDtoMapper.toInStockResponseDtoList(adminService.findAllInStockOrders());
    }

    @GetMapping("/in-stock/{id}")
    public InStockOrderResponseDto findInStockOrderById(@PathVariable UUID id) {
        return orderDtoMapper.toResponseDto(adminService.findInStockOrderById(id));
    }

    @DeleteMapping("/in-stock/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInStockOrder(@PathVariable UUID id) {
        adminService.deleteInStockOrder(id);
    }

    @PostMapping("/configured")
    @ResponseStatus(HttpStatus.CREATED)
    public ConfiguredOrderResponseDto createConfiguredOrder(@Valid @RequestBody ConfiguredOrderRequestDto requestDto) {
        Configurator configurator = new Configurator(adminService.findModelById(requestDto.getModelId()));
        ConfiguredModel configuredModel = configurator.configure(
                findDetail(requestDto.getWheelsId(), Wheels.class, "Wheels"),
                findDetail(requestDto.getSteeringWheelId(), SteeringWheel.class, "Steering wheel"),
                findDetail(requestDto.getTransmissionId(), Transmission.class, "Transmission"),
                findDetail(requestDto.getInteriorId(), Interior.class, "Interior")
        );

        return orderDtoMapper.toResponseDto(orderService.createConfiguredOrder(
                requestDto.getCustomerId(),
                configuredModel
        ));
    }

    @GetMapping("/configured")
    public List<ConfiguredOrderResponseDto> findAllConfiguredOrders() {
        return orderDtoMapper.toConfiguredResponseDtoList(adminService.findAllConfiguredOrders());
    }

    @GetMapping("/configured/{id}")
    public ConfiguredOrderResponseDto findConfiguredOrderById(@PathVariable UUID id) {
        return orderDtoMapper.toResponseDto(adminService.findConfiguredOrderById(id));
    }

    @DeleteMapping("/configured/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteConfiguredOrder(@PathVariable UUID id) {
        adminService.deleteConfiguredOrder(id);
    }

    private <T extends Detail> T findDetail(UUID id, Class<T> expectedType, String detailName) {
        Detail detail = adminService.findDetailById(id);
        if (!expectedType.isInstance(detail)) {
            throw new DomainValidationException(detailName + " id has invalid detail type");
        }
        return expectedType.cast(detail);
    }
}
