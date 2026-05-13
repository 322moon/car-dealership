package org.example.Infrastructure.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.Infrastructure.dto.testDrive.TestDriveRequestDto;
import org.example.Infrastructure.dto.testDrive.TestDriveResponseDto;
import org.example.Infrastructure.utils.TestDriveDtoMapper;
import org.example.lab1.Domain.Services.AdminService;
import org.example.lab1.Domain.Services.TestDriveService;
import org.springframework.http.HttpStatus;
import org.example.security.CurrentUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/test-drives")
@RequiredArgsConstructor
public class TestDriveController {
    private final TestDriveService testDriveService;
    private final AdminService adminService;
    private final TestDriveDtoMapper testDriveDtoMapper;
    private final CurrentUser currentUser;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public TestDriveResponseDto create(@Valid @RequestBody TestDriveRequestDto requestDto) {
        return testDriveDtoMapper.toResponseDto(testDriveService.createRequest(
                currentUser.id(),
                requestDto.getCarId(),
                requestDto.getTestDate()
        ));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public List<TestDriveResponseDto> findAll() {
        return testDriveDtoMapper.toResponseDtoList(testDriveService.findAllRequests());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public TestDriveResponseDto findById(@PathVariable UUID id) {
        return testDriveDtoMapper.toResponseDto(testDriveService.findRequestById(id));
    }

    @PostMapping("/{id}/confirm")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public TestDriveResponseDto confirm(@PathVariable UUID id) {
        testDriveService.confirmRequest(id);
        return testDriveDtoMapper.toResponseDto(testDriveService.findRequestById(id));
    }

    @PostMapping("/cars/{carId}/availability")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public void addCarToTestDriveList(@PathVariable UUID carId) {
        testDriveService.addCarToTestDriveList(carId);
    }

    @DeleteMapping("/cars/{carId}/availability")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public void removeCarFromTestDriveList(@PathVariable UUID carId) {
        testDriveService.removeCarFromTestDriveList(carId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable UUID id) {
        adminService.deleteTestDrive(id);
    }
}
