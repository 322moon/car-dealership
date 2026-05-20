package org.example.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("ENGINE")
@NoArgsConstructor
@Getter
@Setter
public class EngineJpaEntity extends DetailJpaEntity{

    @Column(name = "engine_capacity")
    private double engineCapacity;

    @Column(name = "engine_power")
    private int enginePower;
}
