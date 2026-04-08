package org.example.Infrastructure.Repositories.InMemory;

import org.example.Infrastructure.Repositories.InMemoryRepository;
import org.example.lab1.Domain.Entities.Cars.Car;

import java.util.UUID;

public class CarRepository extends InMemoryRepository<Car, UUID> {

    @Override
    protected UUID getID(Car entity) {
        return entity.getId();
    }
}
