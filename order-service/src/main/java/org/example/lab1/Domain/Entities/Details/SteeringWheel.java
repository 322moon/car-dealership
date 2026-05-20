package org.example.lab1.Domain.Entities.Details;

import lombok.Getter;
import org.example.lab1.Domain.Entities.Cars.Model;
import org.example.lab1.Domain.ValueObjects.Price;
import java.util.List;
import java.util.UUID;

@Getter
public class SteeringWheel extends Detail{

    public SteeringWheel(
            UUID id,
            String name,
            List<UUID> compatibleModels,
            boolean inStock,
            Price price)
    {
        super(id, name, DetailType.STEERING_WHEEL, compatibleModels, inStock, price);
    }
}
