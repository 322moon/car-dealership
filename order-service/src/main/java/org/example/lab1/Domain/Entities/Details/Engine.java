package org.example.lab1.Domain.Entities.Details;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.lab1.Domain.ValueObjects.Price;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class Engine{

    private Price price;
    private final DetailType detailType = DetailType.ENGINE;
    private boolean inStock;
    private final double engineCapacity;
    private final int enginePower;
}
