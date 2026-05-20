package org.example.lab1.Domain.Entities.Details;

import org.example.lab1.Domain.Entities.Cars.Model;

public interface ConfigurableNode {
    boolean isCompatibleWith(Model type);
    void addCompatibleCar(Model type);
}
