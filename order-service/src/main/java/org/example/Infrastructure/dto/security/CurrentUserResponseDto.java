package org.example.Infrastructure.dto.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CurrentUserResponseDto {
    private UUID id;
    private String username;
    private String email;
    private List<String> roles;
}
