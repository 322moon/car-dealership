package org.example.Infrastructure.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.Infrastructure.dto.detail.DetailRequestDto;
import org.example.Infrastructure.dto.detail.DetailResponseDto;
import org.example.Infrastructure.utils.DetailDtoMapper;
import org.example.lab1.Domain.Entities.Details.Detail;
import org.example.lab1.Domain.Services.AdminService;
import org.example.lab1.Domain.Services.WarehouseService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/details")
@RequiredArgsConstructor
public class DetailController {
    private final AdminService adminService;
    private final WarehouseService warehouseService;
    private final DetailDtoMapper detailDtoMapper;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<DetailResponseDto> findAll() {
        return detailDtoMapper.toResponseDtoList(adminService.findAllDetails());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public DetailResponseDto findById(@PathVariable UUID id) {
        return detailDtoMapper.toResponseDto(adminService.findDetailById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public DetailResponseDto create(@Valid @RequestBody DetailRequestDto requestDto) {
        Detail detail = detailDtoMapper.toNewDomain(requestDto);
        warehouseService.addDetail(detail);
        return detailDtoMapper.toResponseDto(detail);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public DetailResponseDto update(@PathVariable UUID id, @Valid @RequestBody DetailRequestDto requestDto) {
        warehouseService.findDetailById(id);
        Detail detail = detailDtoMapper.toExistingDomain(id, requestDto);
        warehouseService.updateDetail(detail);
        return detailDtoMapper.toResponseDto(detail);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable UUID id) {
        adminService.deleteDetail(id);
    }
}
