package org.example.Infrastructure.dto.inStockOrder;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class InStockOrderResponseDto {
    private UUID id;
    private UUID customerId;
    private UUID carId;
    private UUID managerId;
    private BigDecimal totalPrice;
    private Instant createdAt;
    private String status;
}
