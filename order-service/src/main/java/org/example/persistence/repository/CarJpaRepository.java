package org.example.persistence.repository;

import org.example.persistence.entity.CarJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarJpaRepository extends JpaRepository<CarJpaEntity, UUID> {
    Optional<CarJpaEntity> findByIdAndRemovedFalse(UUID id);

    List<CarJpaEntity> findAllByRemovedFalse();
}
