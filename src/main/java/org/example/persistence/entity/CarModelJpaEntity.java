package org.example.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.CarBrand;
import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.CarType;
import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.DriveType;
import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.FuelType;

import java.math.BigDecimal;

@Entity
@Table(name = "models")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CarModelJpaEntity extends BaseJpaEntity {
    @Column(name = "model_name", nullable = false)
    private String name;

    @Column(name = "base_price", nullable = false)
    private BigDecimal basePrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "car_type", nullable = false)
    private CarType carType;

    @Enumerated(EnumType.STRING)
    @Column(name = "car_brand", nullable = false)
    private CarBrand carBrand;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type", nullable = false)
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)
    @Column(name = "drive_type", nullable = false)
    private DriveType driveType;

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
