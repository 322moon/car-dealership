package org.example.Infrastructure.Repositories.InMemory;

import org.example.Infrastructure.Repositories.InMemoryRepository;
import org.example.lab1.Domain.Entities.Sales.OrderInStock;

import java.util.UUID;

public class OrderInStockRepository extends InMemoryRepository<OrderInStock, UUID> {
    @Override
    protected UUID getID(OrderInStock entity) {
        return entity.getId();
    }
}
