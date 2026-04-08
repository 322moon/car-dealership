package org.example.lab1.Domain.Entities.Users;

import lombok.*;
import org.example.exception.DomainValidationExceptions;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private UUID id;
    private String email;
    private String fullName;
    private List<Role> roles;

    public static User create(String email, String fullName, List<Role> roles){
        return new User(UUID.randomUUID(), email, fullName, roles);
    }


}
