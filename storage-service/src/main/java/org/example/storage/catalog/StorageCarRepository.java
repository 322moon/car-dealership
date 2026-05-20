package org.example.storage.catalog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StorageCarRepository extends JpaRepository<StorageCarJpaEntity, UUID> {
    List<StorageCarJpaEntity> findAllByRemovedFalse();

    Optional<StorageCarJpaEntity> findByIdAndRemovedFalse(UUID id);
}
