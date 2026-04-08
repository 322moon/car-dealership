package org.example.lab1.Domain.Entities.Details;

import lombok.Getter;
import org.example.lab1.Domain.Entities.Cars.Model;
import org.example.lab1.Domain.ValueObjects.Price;
import java.util.List;
import java.util.UUID;

@Getter
public class Wheels extends Detail{
    public Wheels(
            UUID id,
            String name,
            List<UUID> compatibleModels,
            boolean inStock,
            Price price)
    {
        super(id, name, DetailType.WHEELS, compatibleModels, inStock, price);
    }
}
