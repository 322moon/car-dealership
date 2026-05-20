package org.example.Infrastructure.utils;

import org.example.Infrastructure.dto.testDrive.TestDriveResponseDto;
import org.example.lab1.Domain.Entities.TestDrive;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TestDriveDtoMapper {
    default TestDriveResponseDto toResponseDto(TestDrive testDrive) {
        if (testDrive == null) {
            return null;
        }

        TestDriveResponseDto dto = new TestDriveResponseDto();
        dto.setId(testDrive.getId());
        dto.setCustomerId(testDrive.getCustomerId());
        dto.setCarId(testDrive.getCarId());
        dto.setTestDate(testDrive.getTestDate());
        dto.setConfirmed(testDrive.isConfirm());
        return dto;
    }

    default List<TestDriveResponseDto> toResponseDtoList(List<TestDrive> testDrives) {
        return testDrives.stream()
                .map(this::toResponseDto)
                .toList();
    }
}
