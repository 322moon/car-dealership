package org.example.Infrastructure.dto.inStockOrder;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class InStockOrderRequestDto {
    @NotNull
    private UUID customerId;

    @NotNull
    private UUID carId;
}
