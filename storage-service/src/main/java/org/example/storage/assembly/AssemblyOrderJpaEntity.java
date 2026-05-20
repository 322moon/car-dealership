package org.example.storage.assembly;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "assembly_orders")
@Getter
@Setter
public class AssemblyOrderJpaEntity {
    @Id
    private UUID id;

    @Column(name = "source_order_id", nullable = false)
    private UUID sourceOrderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_order_type", nullable = false)
    private SourceOrderType sourceOrderType;

    @Column(name = "car_id")
    private UUID carId;

    @Column(name = "model_id")
    private UUID modelId;

    @Column(name = "warehouse_employee_id")
    private UUID warehouseEmployeeId;

    @Column(name = "trace_id", nullable = false)
    private String traceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AssemblyOrderStatus status;

    @Column(name = "fail_reason")
    private String failReason;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "removed", nullable = false)
    private boolean removed;

    @PrePersist
    void prePersist() {
        Instant now = Instant.now();
        if (id == null) {
            id = UUID.randomUUID();
        }
        createdAt = now;
        updatedAt = now;
        status = status == null ? AssemblyOrderStatus.CREATED : status;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }
}
