package org.example.Infrastructure.dto.testDrive;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class TestDriveRequestDto {
    @NotNull
    private UUID customerId;

    @NotNull
    private UUID carId;

    @NotNull
    private Instant testDate;
}
