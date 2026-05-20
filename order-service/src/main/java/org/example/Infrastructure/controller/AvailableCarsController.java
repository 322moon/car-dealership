package org.example.Infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.example.Infrastructure.dto.availablecar.AvailableCarResponseDto;
import org.example.Infrastructure.grpc.AvailableCarsGrpcClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
public class AvailableCarsController {
    private final AvailableCarsGrpcClient availableCarsGrpcClient;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public List<AvailableCarResponseDto> findAvailableCars() {
        return availableCarsGrpcClient.findAvailableCars();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public AvailableCarResponseDto findAvailableCarById(@PathVariable UUID id) {
        return availableCarsGrpcClient.findAvailableCarById(id);
    }
}
