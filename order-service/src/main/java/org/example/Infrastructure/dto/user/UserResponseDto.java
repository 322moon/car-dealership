package org.example.Infrastructure.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.example.lab1.Domain.Entities.Users.Role;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UserResponseDto {
    private UUID id;
    private String email;
    private String fullName;
    private List<Role> roles;
}
