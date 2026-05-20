package org.example.storage.assembly;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssemblyOrderRepository extends JpaRepository<AssemblyOrderJpaEntity, UUID> {
    List<AssemblyOrderJpaEntity> findAllByRemovedFalse();

    Optional<AssemblyOrderJpaEntity> findByIdAndRemovedFalse(UUID id);

    boolean existsBySourceOrderIdAndRemovedFalse(UUID sourceOrderId);
}
