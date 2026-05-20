package org.example.storage.assembly;

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
@RequestMapping("/api/assembly-orders")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
public class AssemblyOrderController {
    private final AssemblyOrderService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AssemblyOrderResponseDto create(@Valid @RequestBody AssemblyOrderRequestDto request) {
        return toResponse(service.create(request));
    }

    @GetMapping
    public List<AssemblyOrderResponseDto> findAll() {
        return service.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public AssemblyOrderResponseDto findById(@PathVariable UUID id) {
        return toResponse(service.findById(id));
    }

    @PutMapping("/{id}")
    public AssemblyOrderResponseDto update(@PathVariable UUID id, @Valid @RequestBody AssemblyOrderUpdateDto request) {
        return toResponse(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    private AssemblyOrderResponseDto toResponse(AssemblyOrderJpaEntity entity) {
        AssemblyOrderResponseDto dto = new AssemblyOrderResponseDto();
        dto.setId(entity.getId());
        dto.setSourceOrderId(entity.getSourceOrderId());
        dto.setSourceOrderType(entity.getSourceOrderType());
        dto.setCarId(entity.getCarId());
        dto.setModelId(entity.getModelId());
        dto.setWarehouseEmployeeId(entity.getWarehouseEmployeeId());
        dto.setTraceId(entity.getTraceId());
        dto.setStatus(entity.getStatus());
        dto.setFailReason(entity.getFailReason());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
