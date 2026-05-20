package org.example.storage.assembly;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AssemblyOrderUpdateDto {
    private UUID warehouseEmployeeId;
    private AssemblyOrderStatus status;
    private String failReason;
}
