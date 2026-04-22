package org.example.Infrastructure.Repositories.database;

import lombok.RequiredArgsConstructor;
import org.example.exception.NotFoundException;
import org.example.lab1.Domain.Entities.Users.User;
import org.example.lab1.Domain.repository.BaseRepository;
import org.example.persistence.entity.UserJpaEntity;
import org.example.persistence.repository.UserJpaRepository;
import org.example.persistence.utils.UserPersistenceMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserRepositoryAdapter implements BaseRepository<User, UUID> {
    private final UserJpaRepository userJpaRepository;
    private final UserPersistenceMapper userPersistenceMapper;

    @Override
    @Transactional
    public void save(User user) {
        UserJpaEntity entity = userJpaRepository.findById(user.getId())
                .orElseGet(UserJpaEntity::new);

        entity.setId(user.getId());
        entity.setEmail(user.getEmail());
        entity.setFullName(user.getFullName());
        entity.setRoles(user.getRoles());
        entity.setRemoved(false);

        userJpaRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(UUID uuid) {
        UserJpaEntity entity = userJpaRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException.EntityNotFoundException("User not found"));
        entity.setRemoved(true);
        userJpaRepository.save(entity);
    }

    @Override
    public User findById(UUID uuid) {
        return userJpaRepository.findByIdAndRemovedFalse(uuid)
                .map(userPersistenceMapper::toDomain)
                .orElseThrow(() -> new NotFoundException.EntityNotFoundException("User not found"));
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAllByRemovedFalse().stream()
                .map(userPersistenceMapper::toDomain)
                .toList();
    }
}
