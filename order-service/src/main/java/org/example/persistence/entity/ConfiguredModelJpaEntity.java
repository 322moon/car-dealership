package org.example.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "configured_models")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConfiguredModelJpaEntity extends BaseJpaEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    private CarModelJpaEntity model;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "engine_id", nullable = false)
    private EngineJpaEntity engine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transmission_id", nullable = false)
    private TransmissionJpaEntity transmission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wheels_id", nullable = false)
    private WheelsJpaEntity wheels;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "steering_wheel_id", nullable = false)
    private SteeringWheelJpaEntity steeringWheel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interior_id", nullable = false)
    private InteriorJpaEntity interior;
}
