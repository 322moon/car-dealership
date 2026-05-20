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
public class StorageDetailDto {
    private UUID id;

    @NotBlank
    private String detailType;

    @NotBlank
    private String name;

    @NotNull
    private BigDecimal price;

    private Instant createdAt;
    private Instant updatedAt;
}
