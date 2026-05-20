package org.example.storage.catalog;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/storage/cars")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
public class StorageCarController {
    private final StorageCatalogService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StorageCarDto create(@Valid @RequestBody StorageCarDto dto) {
        dto.setId(null);
        return toDto(service.saveCar(dto));
    }

    @GetMapping
    public List<StorageCarDto> findAll() {
        return service.findAllCars().stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public StorageCarDto findById(@PathVariable UUID id) {
        return toDto(service.findCar(id));
    }

    @PutMapping("/{id}")
    public StorageCarDto update(@PathVariable UUID id, @Valid @RequestBody StorageCarDto dto) {
        dto.setId(id);
        return toDto(service.saveCar(dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.deleteCar(id);
    }

    private StorageCarDto toDto(StorageCarJpaEntity entity) {
        StorageCarDto dto = new StorageCarDto();
        dto.setId(entity.getId());
        dto.setModelId(entity.getModelId());
        dto.setColor(entity.getColor());
        dto.setPrice(entity.getPrice());
        dto.setAvailableForTestDrive(entity.isAvailableForTestDrive());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
