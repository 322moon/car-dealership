package org.example.Infrastructure.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.Infrastructure.dto.configuredOrder.ConfiguredOrderRequestDto;
import org.example.Infrastructure.dto.configuredOrder.ConfiguredOrderResponseDto;
import org.example.Infrastructure.dto.inStockOrder.InStockOrderRequestDto;
import org.example.Infrastructure.dto.inStockOrder.InStockOrderResponseDto;
import org.example.Infrastructure.query.SecuredOrderQueryService;
import org.example.Infrastructure.utils.OrderDtoMapper;
import org.example.exception.DomainValidationExceptions.DomainValidationException;
import org.example.lab1.Domain.Entities.Cars.ConfiguredModel;
import org.example.lab1.Domain.Entities.Sales.ConfiguredCarOrder;
import org.example.lab1.Domain.Entities.Sales.OrderInStock;
import org.example.lab1.Domain.Entities.Details.Detail;
import org.example.lab1.Domain.Entities.Details.Interior;
import org.example.lab1.Domain.Entities.Details.SteeringWheel;
import org.example.lab1.Domain.Entities.Details.Transmission;
import org.example.lab1.Domain.Entities.Details.Wheels;
import org.example.lab1.Domain.configurator.Configurator;
import org.example.lab1.Domain.Services.AdminService;
import org.example.lab1.Domain.Services.OrderService;
import org.example.security.CurrentUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrdersController {
    private final OrderService orderService;
    private final AdminService adminService;
    private final SecuredOrderQueryService securedOrderQueryService;
    private final CurrentUser currentUser;
    private final OrderDtoMapper orderDtoMapper;

    @PostMapping("/in-stock")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public InStockOrderResponseDto createInStockOrder(@Valid @RequestBody InStockOrderRequestDto requestDto) {
        return orderDtoMapper.toResponseDto(orderService.createOrderInStock(
                currentUser.id(),
                requestDto.getCarId()
        ));
    }

    @GetMapping("/in-stock")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public List<InStockOrderResponseDto> findAllInStockOrders() {
        return orderDtoMapper.toInStockResponseDtoList(securedOrderQueryService.findVisibleInStockOrders());
    }

    @GetMapping("/in-stock/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN') or (hasRole('USER') and @orderSecurity.isInStockOrderOwner(#id))")
    public InStockOrderResponseDto findInStockOrderById(@PathVariable UUID id) {
        return orderDtoMapper.toResponseDto(adminService.findInStockOrderById(id));
    }

    @PostMapping("/in-stock/{id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @orderSecurity.isInStockOrderOwner(#id))")
    public void cancelInStockOrder(@PathVariable UUID id) {
        updateInStockOrder(id, OrderInStock::cancel);
    }

    @PostMapping("/in-stock/{id}/approve-by-manager")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public InStockOrderResponseDto approveInStockOrderByManager(@PathVariable UUID id) {
        return orderDtoMapper.toResponseDto(updateInStockOrder(id, OrderInStock::approveByManager));
    }

    @PostMapping("/in-stock/{id}/awaiting-payment")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public InStockOrderResponseDto moveInStockOrderToAwaitingPayment(@PathVariable UUID id) {
        return orderDtoMapper.toResponseDto(updateInStockOrder(id, OrderInStock::moveToAwaitingPayment));
    }

    @PostMapping("/in-stock/{id}/paid")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public InStockOrderResponseDto markInStockOrderPaid(@PathVariable UUID id) {
        return orderDtoMapper.toResponseDto(updateInStockOrder(id, OrderInStock::markPaid));
    }

    @PostMapping("/in-stock/{id}/ready-for-pickup")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public InStockOrderResponseDto markInStockOrderReadyForPickup(@PathVariable UUID id) {
        return orderDtoMapper.toResponseDto(updateInStockOrder(id, OrderInStock::markReadyForPickUp));
    }

    @PostMapping("/in-stock/{id}/complete")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public InStockOrderResponseDto completeInStockOrder(@PathVariable UUID id) {
        return orderDtoMapper.toResponseDto(updateInStockOrder(id, OrderInStock::complete));
    }

    @DeleteMapping("/in-stock/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteInStockOrder(@PathVariable UUID id) {
        adminService.deleteInStockOrder(id);
    }

    @PostMapping("/configured")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ConfiguredOrderResponseDto createConfiguredOrder(@Valid @RequestBody ConfiguredOrderRequestDto requestDto) {
        Configurator configurator = new Configurator(adminService.findModelById(requestDto.getModelId()));
        ConfiguredModel configuredModel = configurator.configure(
                findDetail(requestDto.getWheelsId(), Wheels.class, "Wheels"),
                findDetail(requestDto.getSteeringWheelId(), SteeringWheel.class, "Steering wheel"),
                findDetail(requestDto.getTransmissionId(), Transmission.class, "Transmission"),
                findDetail(requestDto.getInteriorId(), Interior.class, "Interior")
        );

        return orderDtoMapper.toResponseDto(orderService.createConfiguredOrder(
                currentUser.id(),
                configuredModel
        ));
    }

    @GetMapping("/configured")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public List<ConfiguredOrderResponseDto> findAllConfiguredOrders() {
        return orderDtoMapper.toConfiguredResponseDtoList(securedOrderQueryService.findVisibleConfiguredOrders());
    }

    @GetMapping("/configured/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN') or (hasRole('USER') and @orderSecurity.isConfiguredOrderOwner(#id))")
    public ConfiguredOrderResponseDto findConfiguredOrderById(@PathVariable UUID id) {
        return orderDtoMapper.toResponseDto(adminService.findConfiguredOrderById(id));
    }

    @PostMapping("/configured/{id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @orderSecurity.isConfiguredOrderOwner(#id))")
    public void cancelConfiguredOrder(@PathVariable UUID id) {
        updateConfiguredOrder(id, ConfiguredCarOrder::cancel);
    }

    @PostMapping("/configured/{id}/approve-by-warehouse")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public ConfiguredOrderResponseDto approveConfiguredOrderByWarehouse(@PathVariable UUID id) {
        return orderDtoMapper.toResponseDto(updateConfiguredOrder(id, ConfiguredCarOrder::approveByWarehouse));
    }

    @PostMapping("/configured/{id}/awaiting-payment")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ConfiguredOrderResponseDto moveConfiguredOrderToAwaitingPayment(@PathVariable UUID id) {
        return orderDtoMapper.toResponseDto(updateConfiguredOrder(id, ConfiguredCarOrder::moveToAwaitingPayment));
    }

    @PostMapping("/configured/{id}/paid")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ConfiguredOrderResponseDto markConfiguredOrderPaid(@PathVariable UUID id) {
        return orderDtoMapper.toResponseDto(updateConfiguredOrder(id, ConfiguredCarOrder::markPaid));
    }

    @PostMapping("/configured/{id}/awaiting-delivery")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ConfiguredOrderResponseDto markConfiguredOrderAwaitingDelivery(@PathVariable UUID id) {
        return orderDtoMapper.toResponseDto(updateConfiguredOrder(id, ConfiguredCarOrder::markAwaitingDelivery));
    }

    @PostMapping("/configured/{id}/ready-for-pickup")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public ConfiguredOrderResponseDto markConfiguredOrderReadyForPickup(@PathVariable UUID id) {
        return orderDtoMapper.toResponseDto(updateConfiguredOrder(id, ConfiguredCarOrder::markReadyForPickUp));
    }

    @PostMapping("/configured/{id}/complete")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ConfiguredOrderResponseDto completeConfiguredOrder(@PathVariable UUID id) {
        return orderDtoMapper.toResponseDto(updateConfiguredOrder(id, ConfiguredCarOrder::complete));
    }

    @DeleteMapping("/configured/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
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

    private OrderInStock updateInStockOrder(UUID id, java.util.function.Consumer<OrderInStock> operation) {
        OrderInStock order = adminService.findInStockOrderById(id);
        operation.accept(order);
        adminService.saveInStockOrder(order);
        return order;
    }

    private ConfiguredCarOrder updateConfiguredOrder(UUID id, java.util.function.Consumer<ConfiguredCarOrder> operation) {
        ConfiguredCarOrder order = adminService.findConfiguredOrderById(id);
        operation.accept(order);
        adminService.saveConfiguredOrder(order);
        return order;
    }
}
