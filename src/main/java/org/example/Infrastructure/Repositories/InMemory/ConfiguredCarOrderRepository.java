package org.example.Infrastructure.Repositories.InMemory;

import org.example.Infrastructure.Repositories.InMemoryRepository;
import org.example.lab1.Domain.Entities.Sales.ConfiguredCarOrder;

import java.util.UUID;

public class ConfiguredCarOrderRepository extends InMemoryRepository<ConfiguredCarOrder, UUID> {

    @Override
    protected UUID getID(ConfiguredCarOrder entity) {
        return entity.getId();
    }
}
