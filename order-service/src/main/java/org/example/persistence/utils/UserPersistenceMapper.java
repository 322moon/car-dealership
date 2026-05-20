package org.example.persistence.utils;

import org.example.lab1.Domain.Entities.Users.User;
import org.example.persistence.entity.UserJpaEntity;
import org.mapstruct.Mapper;

import java.util.ArrayList;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {
    default User toDomain(UserJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new User(
                entity.getId(),
                entity.getEmail(),
                entity.getFullName(),
                new ArrayList<>(entity.getRoles())
        );
    }

    default UserJpaEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId());
        entity.setEmail(user.getEmail());
        entity.setFullName(user.getFullName());
        entity.setRoles(new ArrayList<>(user.getRoles()));
        entity.setRemoved(false);
        return entity;
    }
}
