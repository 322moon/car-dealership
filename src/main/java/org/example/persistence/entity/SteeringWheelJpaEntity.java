package org.example.persistence.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("STEERING_WHEEL")
@NoArgsConstructor
@Getter
@Setter
public class SteeringWheelJpaEntity extends DetailJpaEntity{
}
