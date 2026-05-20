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
@RequestMapping("/api/storage/details")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
public class StorageDetailController {
    private final StorageCatalogService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StorageDetailDto create(@Valid @RequestBody StorageDetailDto dto) {
        dto.setId(null);
        return toDto(service.saveDetail(dto));
    }

    @GetMapping
    public List<StorageDetailDto> findAll() {
        return service.findAllDetails().stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public StorageDetailDto findById(@PathVariable UUID id) {
        return toDto(service.findDetail(id));
    }

    @PutMapping("/{id}")
    public StorageDetailDto update(@PathVariable UUID id, @Valid @RequestBody StorageDetailDto dto) {
        dto.setId(id);
        return toDto(service.saveDetail(dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.deleteDetail(id);
    }

    private StorageDetailDto toDto(StorageDetailJpaEntity entity) {
        StorageDetailDto dto = new StorageDetailDto();
        dto.setId(entity.getId());
        dto.setDetailType(entity.getDetailType());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
