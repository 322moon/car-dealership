package org.example.persistence.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("WHEELS")
@NoArgsConstructor
@Getter
@Setter
public class WheelsJpaEntity extends DetailJpaEntity{
}
