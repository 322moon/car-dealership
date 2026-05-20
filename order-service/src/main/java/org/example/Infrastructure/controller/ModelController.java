package org.example.Infrastructure.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.Infrastructure.dto.model.ModelRequestDto;
import org.example.Infrastructure.dto.model.ModelResponseDto;
import org.example.Infrastructure.query.ModelQueryService;
import org.example.Infrastructure.utils.ModelDtoMapper;
import org.example.exception.DomainValidationExceptions.DomainValidationException;
import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.CarBrand;
import org.example.lab1.Domain.Entities.Cars.Model;
import org.example.lab1.Domain.Entities.Details.Detail;
import org.example.lab1.Domain.Entities.Details.Interior;
import org.example.lab1.Domain.Entities.Details.SteeringWheel;
import org.example.lab1.Domain.Entities.Details.Transmission;
import org.example.lab1.Domain.Entities.Details.Wheels;
import org.example.lab1.Domain.Services.AdminService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/models")
@RequiredArgsConstructor
@Profile("monolith")
public class ModelController {
    private final AdminService adminService;
    private final ModelQueryService modelQueryService;
    private final ModelDtoMapper modelDtoMapper;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<ModelResponseDto> findAll(){
        return modelDtoMapper.toResponseDtoList(adminService.findAllModels());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelResponseDto findById(@PathVariable UUID id){
        return modelDtoMapper.toResponseDto(adminService.findModelById(id));
    }

    @GetMapping("/filter")
    @PreAuthorize("isAuthenticated()")
    public List<ModelResponseDto> findByFilter(
            @RequestParam(required = false) CarBrand brand,
            @RequestParam(required = false) UUID engineId,
            @RequestParam(required = false) UUID transmissionId,
            @RequestParam(required = false) UUID wheelsId,
            @RequestParam(required = false) UUID steeringWheelId,
            @RequestParam(required = false) UUID interiorId
    ) {
        return modelDtoMapper.toResponseDtoList(modelQueryService.findByFilter(
                brand,
                engineId,
                transmissionId,
                wheelsId,
                steeringWheelId,
                interiorId
        ));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ModelResponseDto create(@Valid @RequestBody ModelRequestDto modelRequestDto){
        Model model = modelDtoMapper.toNewDomain(modelRequestDto,
                findDetail(modelRequestDto.getTransmissionId(), Transmission.class, "Transmission"),
                findDetail(modelRequestDto.getWheelsId(), Wheels.class, "Wheels"),
                findDetail(modelRequestDto.getSteeringWheelId(), SteeringWheel.class, "Steering wheel"),
                findDetail(modelRequestDto.getInteriorId(), Interior.class, "Interior")
                );
        adminService.saveModel(model);
        return modelDtoMapper.toResponseDto(model);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelResponseDto update(@PathVariable UUID id, @Valid @RequestBody ModelRequestDto modelRequestDto){
        adminService.findModelById(id);
        Model model = modelDtoMapper.toExistingDomain(id, modelRequestDto,
                findDetail(modelRequestDto.getTransmissionId(), Transmission.class, "Transmission"),
                findDetail(modelRequestDto.getWheelsId(), Wheels.class, "Wheels"),
                findDetail(modelRequestDto.getSteeringWheelId(), SteeringWheel.class, "Steering wheel"),
                findDetail(modelRequestDto.getInteriorId(), Interior.class, "Interior")
        );
        adminService.saveModel(model);
        return modelDtoMapper.toResponseDto(model);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable UUID id) {
        adminService.deleteModel(id);
    }

    private <T extends Detail> T findDetail(UUID id, Class<T> expectedType, String detailName) {
        Detail detail = adminService.findDetailById(id);
        if (!expectedType.isInstance(detail)) {
            throw new DomainValidationException(detailName + " id has invalid detail type");
        }
        return expectedType.cast(detail);
    }
}
