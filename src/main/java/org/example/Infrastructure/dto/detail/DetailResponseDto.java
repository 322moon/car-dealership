package org.example.Infrastructure.dto.detail;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class DetailResponseDto {
    private UUID id;
    private String name;
    private String type;
    private List<UUID> compatibleModelIds;
    private boolean inStock;
    private BigDecimal price;
    private String transmissionType;
}
