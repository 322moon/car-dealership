package org.example.persistence.repository;

import org.example.persistence.entity.CarModelJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarModelJpaRepository extends JpaRepository<CarModelJpaEntity, UUID>, JpaSpecificationExecutor<CarModelJpaEntity> {
    Optional<CarModelJpaEntity> findByIdAndRemovedFalse(UUID id);

    List<CarModelJpaEntity> findAllByRemovedFalse();

    Optional<CarModelJpaEntity> findByNameIgnoreCaseAndRemovedFalse(String name);
}
