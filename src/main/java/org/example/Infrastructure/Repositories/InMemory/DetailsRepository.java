package org.example.Infrastructure.Repositories.InMemory;

import org.example.Infrastructure.Repositories.InMemoryRepository;
import org.example.lab1.Domain.Entities.Details.Detail;

import java.util.UUID;

public class DetailsRepository extends InMemoryRepository<Detail, UUID> {

    @Override
    protected UUID getID(Detail entity) {
        return entity.getId();
    }
}
