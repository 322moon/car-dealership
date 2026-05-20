package org.example.storage.catalog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class StorageCarDto {
    private UUID id;

    @NotNull
    private UUID modelId;

    @NotBlank
    private String color;

    @NotNull
    private BigDecimal price;

    private boolean availableForTestDrive;
    private Instant createdAt;
    private Instant updatedAt;
}
