package org.example.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.CarColor;

import java.math.BigDecimal;

@Entity
@Table(name = "cars")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CarJpaEntity extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    private CarModelJpaEntity model;

    @Enumerated(EnumType.STRING)
    @Column(name = "color", nullable = false)
    private CarColor color;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "in_stock", nullable = false)
    private boolean inStock;

    @Column(name = "is_available_for_test_drive", nullable = false)
    private boolean isAvailableForTestDrive;
}
