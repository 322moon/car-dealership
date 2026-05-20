package org.example.Infrastructure.utils;

import org.example.Infrastructure.dto.user.UserRequestDto;
import org.example.Infrastructure.dto.user.UserResponseDto;
import org.example.lab1.Domain.Entities.Users.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {
    UserResponseDto toResponseDto(User user);

    List<UserResponseDto> toResponseDtoList(List<User> users);

    default User toNewDomain(UserRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }

        return User.create(requestDto.getEmail(), requestDto.getFullName(), requestDto.getRoles());
    }
}
