package org.example.persistence.repository;

import org.example.persistence.entity.DetailJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DetailJpaRepository extends JpaRepository<DetailJpaEntity, UUID> {
    Optional<DetailJpaEntity> findByIdAndRemovedFalse(UUID id);

    List<DetailJpaEntity> findAllByRemovedFalse();

    Optional<DetailJpaEntity> findByNameIgnoreCaseAndRemovedFalse(String name);
}
