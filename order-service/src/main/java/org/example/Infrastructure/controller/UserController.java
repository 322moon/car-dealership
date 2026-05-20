package org.example.Infrastructure.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.Infrastructure.dto.user.UserRequestDto;
import org.example.Infrastructure.dto.user.UserResponseDto;
import org.example.Infrastructure.utils.UserDtoMapper;
import org.example.lab1.Domain.Entities.Users.User;
import org.example.lab1.Domain.Services.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final AdminService adminService;
    private final UserDtoMapper userDtoMapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDto> findAll() {
        return userDtoMapper.toResponseDtoList(adminService.findAllUsers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto findById(@PathVariable UUID id) {
        return userDtoMapper.toResponseDto(adminService.findUserById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto create(@Valid @RequestBody UserRequestDto requestDto) {
        User user = userDtoMapper.toNewDomain(requestDto);
        adminService.saveUser(user);
        return userDtoMapper.toResponseDto(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable UUID id) {
        adminService.deleteUser(id);
    }
}
