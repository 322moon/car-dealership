package org.example.storage.grpc;

import lombok.RequiredArgsConstructor;
import org.example.storage.catalog.StorageCarJpaEntity;
import org.example.storage.catalog.StorageCarRepository;
import org.example.storage.catalog.StorageModelJpaEntity;
import org.example.storage.catalog.StorageModelRepository;
import org.example.storage.stock.StockItemRepository;
import org.example.storage.stock.StockItemType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageCarAvailabilityQueryService {
    private final StorageCarRepository carRepository;
    private final StorageModelRepository modelRepository;
    private final StockItemRepository stockItemRepository;

    @Transactional(readOnly = true)
    public List<AvailableCarView> findAvailableCars() {
        return carRepository.findAllByRemovedFalse().stream()
                .filter(car -> isAvailableInStock(car.getId()))
                .map(this::toView)
                .flatMap(Optional::stream)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<AvailableCarView> findAvailableCarById(UUID id) {
        return carRepository.findByIdAndRemovedFalse(id)
                .filter(car -> isAvailableInStock(car.getId()))
                .flatMap(this::toView);
    }

    private boolean isAvailableInStock(UUID carId) {
        return stockItemRepository.findFirstByItemTypeAndItemIdAndRemovedFalse(StockItemType.CAR, carId)
                .map(stockItem -> stockItem.hasAvailableUnit())
                .orElse(false);
    }

    private Optional<AvailableCarView> toView(StorageCarJpaEntity car) {
        return modelRepository.findByIdAndRemovedFalse(car.getModelId())
                .map(model -> toView(car, model));
    }

    private AvailableCarView toView(StorageCarJpaEntity car, StorageModelJpaEntity model) {
        return new AvailableCarView(
                car.getId(),
                car.getModelId(),
                model.getName(),
                model.getBrand(),
                car.getColor(),
                car.getPrice()
        );
    }
}
