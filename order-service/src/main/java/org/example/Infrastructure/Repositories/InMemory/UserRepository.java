package org.example.Infrastructure.Repositories.InMemory;

import org.example.Infrastructure.Repositories.InMemoryRepository;
import org.example.lab1.Domain.Entities.Users.User;

import java.util.UUID;

public class UserRepository extends InMemoryRepository<User, UUID> {
    @Override
    protected UUID getID(User entity) {
        return entity.getId();
    }
}
