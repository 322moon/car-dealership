package org.example.lab1.Domain.Entities.Details;

import lombok.Getter;
import org.example.lab1.Domain.Entities.Cars.Model;
import org.example.lab1.Domain.ValueObjects.Price;

import java.util.List;
import java.util.UUID;

@Getter
public class Interior extends Detail{

    public Interior(
            UUID id,
            String name,
            List<UUID> compatibleModels,
            boolean inStock,
            Price price)
    {
        super(id, name, DetailType.INTERIOR, compatibleModels, inStock, price);
    }
}
