package org.example.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.TransmissionType;

@Entity
@DiscriminatorValue("TRANSMISSION")
@NoArgsConstructor
@Getter
@Setter
public class TransmissionJpaEntity extends DetailJpaEntity{

    @Enumerated(EnumType.STRING)
    @Column(name = "transmission_type")
    private TransmissionType transmissionType;
}
