package org.example.lab1.Domain.Entities.Users;

import java.util.List;
import java.util.UUID;

public class Manager extends User{
    public Manager(String email, String fullName){
        super(UUID.randomUUID(), email, fullName, List.of(Role.MANAGER));
    }
}
