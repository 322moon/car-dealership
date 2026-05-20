package org.example.storage.assembly;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class AssemblyOrderResponseDto {
    private UUID id;
    private UUID sourceOrderId;
    private SourceOrderType sourceOrderType;
    private UUID carId;
    private UUID modelId;
    private UUID warehouseEmployeeId;
    private String traceId;
    private AssemblyOrderStatus status;
    private String failReason;
    private Instant createdAt;
    private Instant updatedAt;
}
