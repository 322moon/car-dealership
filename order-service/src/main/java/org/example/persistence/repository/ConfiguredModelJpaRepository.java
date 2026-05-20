package org.example.persistence.repository;

import org.example.persistence.entity.ConfiguredModelJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ConfiguredModelJpaRepository extends JpaRepository<ConfiguredModelJpaEntity, UUID> {
    Optional<ConfiguredModelJpaEntity> findByIdAndRemovedFalse(UUID id);
}
