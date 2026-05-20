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
@RequestMapping("/api/storage/models")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
public class StorageModelController {
    private final StorageCatalogService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StorageModelDto create(@Valid @RequestBody StorageModelDto dto) {
        dto.setId(null);
        return toDto(service.saveModel(dto));
    }

    @GetMapping
    public List<StorageModelDto> findAll() {
        return service.findAllModels().stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public StorageModelDto findById(@PathVariable UUID id) {
        return toDto(service.findModel(id));
    }

    @PutMapping("/{id}")
    public StorageModelDto update(@PathVariable UUID id, @Valid @RequestBody StorageModelDto dto) {
        dto.setId(id);
        return toDto(service.saveModel(dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.deleteModel(id);
    }

    private StorageModelDto toDto(StorageModelJpaEntity entity) {
        StorageModelDto dto = new StorageModelDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setBrand(entity.getBrand());
        dto.setBasePrice(entity.getBasePrice());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
