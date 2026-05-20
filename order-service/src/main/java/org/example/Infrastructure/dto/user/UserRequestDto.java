package org.example.Infrastructure.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.lab1.Domain.Entities.Users.Role;

import java.util.List;

@Getter
@Setter
public class UserRequestDto {
    @NotNull
    private String email;

    @NotNull
    private String fullName;

    @NotNull
    private List<Role> roles;
}
