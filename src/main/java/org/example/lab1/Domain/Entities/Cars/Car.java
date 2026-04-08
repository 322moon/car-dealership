package org.example.lab1.Domain.Entities.Cars;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.CarColor;
import org.example.lab1.Domain.ValueObjects.Price;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Car{

    private UUID id;
    private Model model;
    private CarColor color;
    private Price price;
    private boolean inStock;
    private boolean isAvailableForTestDrive;

    public static Car create(Model model, CarColor color, Price price, boolean inStock, boolean isAvailableForTestDrive){
        return new Car(UUID.randomUUID(), model, color, price, inStock, isAvailableForTestDrive);
    }
}
