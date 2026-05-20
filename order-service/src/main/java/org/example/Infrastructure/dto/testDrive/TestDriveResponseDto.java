package org.example.Infrastructure.dto.testDrive;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class TestDriveResponseDto {
    private UUID id;
    private UUID customerId;
    private UUID carId;
    private Instant testDate;
    private boolean confirmed;
}
