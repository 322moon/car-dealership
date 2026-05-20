package org.example.storage.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockReservationService {
    private final StockItemRepository stockItemRepository;

    public ReservationResult reserveCar(UUID carId) {
        return reserve(StockItemType.CAR, List.of(carId));
    }

    public ReservationResult reserveDetails(List<UUID> detailIds) {
        return reserve(StockItemType.DETAIL, detailIds);
    }

    private ReservationResult reserve(StockItemType itemType, List<UUID> itemIds) {
        List<StockItemJpaEntity> stockItems = new ArrayList<>();
        for (UUID itemId : itemIds) {
            StockItemJpaEntity stockItem = stockItemRepository.findByItemTypeAndItemIdAndRemovedFalse(itemType, itemId)
                    .orElse(null);
            if (stockItem == null || !stockItem.hasAvailableUnit()) {
                return ReservationResult.fail("No available stock for " + itemType + " " + itemId);
            }
            stockItems.add(stockItem);
        }

        for (StockItemJpaEntity stockItem : stockItems) {
            stockItem.reserveOne();
            stockItemRepository.save(stockItem);
        }
        return ReservationResult.success();
    }
}
