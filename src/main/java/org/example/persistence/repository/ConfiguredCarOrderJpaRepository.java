package org.example.persistence.repository;

import org.example.persistence.entity.ConfiguredOrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConfiguredCarOrderJpaRepository extends JpaRepository<ConfiguredOrderJpaEntity, UUID> {
    Optional<ConfiguredOrderJpaEntity> findByIdAndRemovedFalse(UUID id);

    List<ConfiguredOrderJpaEntity> findAllByRemovedFalse();
}
