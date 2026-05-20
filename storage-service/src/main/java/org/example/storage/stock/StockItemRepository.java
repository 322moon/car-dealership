package org.example.storage.stock;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;
import java.util.UUID;

public interface StockItemRepository extends JpaRepository<StockItemJpaEntity, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<StockItemJpaEntity> findByItemTypeAndItemIdAndRemovedFalse(StockItemType itemType, UUID itemId);
}
