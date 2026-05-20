package org.example.storage.catalog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StorageDetailRepository extends JpaRepository<StorageDetailJpaEntity, UUID> {
    List<StorageDetailJpaEntity> findAllByRemovedFalse();

    Optional<StorageDetailJpaEntity> findByIdAndRemovedFalse(UUID id);
}
