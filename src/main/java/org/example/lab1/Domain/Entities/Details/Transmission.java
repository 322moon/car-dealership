package org.example.lab1.Domain.Entities.Details;

import lombok.Getter;
import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.TransmissionType;
import org.example.lab1.Domain.Entities.Cars.Model;
import org.example.lab1.Domain.ValueObjects.Price;
import java.util.List;
import java.util.UUID;

@Getter
public class Transmission extends Detail{
    private final TransmissionType transmissionType;

    public Transmission(
            UUID id,
            String name,
            TransmissionType transmissionType,
            List<UUID> compatibleModels,
            boolean inStock,
            Price price)
    {
        super(id, name, DetailType.TRANSMISSION, compatibleModels, inStock, price);
        this.transmissionType = transmissionType;
    }
}
