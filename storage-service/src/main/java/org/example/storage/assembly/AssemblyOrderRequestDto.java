package org.example.storage.assembly;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AssemblyOrderRequestDto {
    @NotNull
    private UUID sourceOrderId;

    @NotNull
    private SourceOrderType sourceOrderType;

    private UUID carId;

    private UUID modelId;

    private UUID warehouseEmployeeId;

    @NotBlank
    private String traceId;
}
