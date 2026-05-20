package org.example.lab1.Domain.Entities.Cars;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.lab1.Domain.Entities.Details.Interior;
import org.example.lab1.Domain.Entities.Details.SteeringWheel;
import org.example.lab1.Domain.Entities.Details.Transmission;
import org.example.lab1.Domain.Entities.Details.Wheels;
import org.example.lab1.Domain.ValueObjects.Price;

@Getter
@Setter
@RequiredArgsConstructor
public class ConfiguredModel {
    private final Model model;
    private Price totalPrice;
    private Transmission transmission;
    private Wheels wheels;
    private SteeringWheel steeringWheel;
    private Interior interior;
}
