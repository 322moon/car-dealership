package org.example.Infrastructure.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.Infrastructure.dto.car.CarRequestDto;
import org.example.Infrastructure.dto.car.CarResponseDto;
import org.example.Infrastructure.utils.CarDtoMapper;
import org.example.lab1.Domain.Entities.Cars.Car;
import org.example.lab1.Domain.Entities.Cars.Model;
import org.example.lab1.Domain.Services.AdminService;
import org.example.lab1.Domain.Services.CatalogService;
import org.example.lab1.Domain.Services.WarehouseService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
@Profile("monolith")
public class CarController {
    private final CatalogService catalogService;
    private final WarehouseService warehouseService;
    private final AdminService adminService;
    private final CarDtoMapper carDtoMapper;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<CarResponseDto> findAll() {
        return carDtoMapper.toResponseDtoList(catalogService.findAvailableCars());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public CarResponseDto findById(@PathVariable UUID id) {
        return carDtoMapper.toResponseDto(catalogService.findCarById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public CarResponseDto create(@Valid @RequestBody CarRequestDto requestDto) {
        Model model = adminService.findModelById(requestDto.getModelId());
        Car car = carDtoMapper.toNewDomain(requestDto, model);
        warehouseService.addCar(car);
        return carDtoMapper.toResponseDto(car);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public CarResponseDto update(@PathVariable UUID id, @Valid @RequestBody CarRequestDto requestDto) {
        warehouseService.findCarById(id);
        Model model = adminService.findModelById(requestDto.getModelId());
        Car car = carDtoMapper.toExistingDomain(id, requestDto, model);
        warehouseService.updateCar(car);
        return carDtoMapper.toResponseDto(car);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable UUID id) {
        adminService.deleteCar(id);
    }
}
