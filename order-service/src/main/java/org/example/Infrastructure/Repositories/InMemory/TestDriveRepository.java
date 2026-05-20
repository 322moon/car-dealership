package org.example.Infrastructure.Repositories.InMemory;

import org.example.Infrastructure.Repositories.InMemoryRepository;
import org.example.lab1.Domain.Entities.TestDrive;

import java.util.UUID;

public class TestDriveRepository extends InMemoryRepository<TestDrive, UUID> {
    @Override
    protected UUID getID(TestDrive entity) {
        return entity.getId();
    }
}
