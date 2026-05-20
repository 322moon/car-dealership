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
public class StorageModelDto {
    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    private String brand;

    @NotNull
    private BigDecimal basePrice;

    private Instant createdAt;
    private Instant updatedAt;
}
