package org.example.Infrastructure.dto.detail;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.TransmissionType;
import org.example.lab1.Domain.Entities.Details.DetailType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class DetailRequestDto {
    @NotNull
    private String name;

    @NotNull
    private DetailType type;

    private List<UUID> compatibleModelIds;

    private boolean inStock;

    @NotNull
    private BigDecimal price;

    private TransmissionType transmissionType;
}
