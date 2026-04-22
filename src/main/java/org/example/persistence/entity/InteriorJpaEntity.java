package org.example.persistence.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("INTERIOR")
@NoArgsConstructor
@Getter
@Setter
public class InteriorJpaEntity extends DetailJpaEntity{
}
