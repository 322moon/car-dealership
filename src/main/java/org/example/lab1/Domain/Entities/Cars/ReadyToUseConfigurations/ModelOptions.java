package org.example.lab1.Domain.Entities.Cars.ReadyToUseConfigurations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.lab1.Domain.Entities.Details.Interior;
import org.example.lab1.Domain.Entities.Details.SteeringWheel;
import org.example.lab1.Domain.Entities.Details.Transmission;
import org.example.lab1.Domain.Entities.Details.Wheels;

import java.util.List;

@Getter
@AllArgsConstructor
public class ModelOptions {
    private final List<Wheels> wheels;
    private final List<Transmission> transmissions;
    private final List<SteeringWheel> steeringWheels;
    private final List<Interior> interiors;
}
