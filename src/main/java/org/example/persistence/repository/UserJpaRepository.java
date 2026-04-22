package org.example.persistence.repository;

import org.example.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
    Optional<UserJpaEntity> findByIdAndRemovedFalse(UUID id);

    List<UserJpaEntity> findAllByRemovedFalse();
}
