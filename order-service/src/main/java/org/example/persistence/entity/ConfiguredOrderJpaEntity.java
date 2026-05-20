package org.example.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.lab1.Domain.Entities.Sales.ConfiguredOrderStatus;


import java.math.BigDecimal;

@Entity
@Table(name = "configured_orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConfiguredOrderJpaEntity extends BaseJpaEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private UserJpaEntity customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private UserJpaEntity manager;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "configured_model_id", nullable = false, unique = true)
    private ConfiguredModelJpaEntity configuredModel;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ConfiguredOrderStatus status;
}
