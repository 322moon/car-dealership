package org.example.persistence.repository;

import org.example.persistence.entity.InStockOrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InStockOrderJpaRepository extends JpaRepository<InStockOrderJpaEntity, UUID> {
    Optional<InStockOrderJpaEntity> findByIdAndRemovedFalse(UUID id);

    List<InStockOrderJpaEntity> findAllByRemovedFalse();
}
