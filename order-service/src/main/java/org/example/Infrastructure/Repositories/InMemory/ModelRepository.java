package org.example.Infrastructure.Repositories.InMemory;

import org.example.Infrastructure.Repositories.InMemoryRepository;
import org.example.lab1.Domain.Entities.Cars.Model;
import java.util.UUID;

public class ModelRepository extends InMemoryRepository<Model, UUID> {
    @Override
    protected UUID getID(Model entity) {
        return entity.getId();
    }
}
