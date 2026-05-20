package org.example.storage.catalog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StorageModelRepository extends JpaRepository<StorageModelJpaEntity, UUID> {
    List<StorageModelJpaEntity> findAllByRemovedFalse();

    Optional<StorageModelJpaEntity> findByIdAndRemovedFalse(UUID id);
}
