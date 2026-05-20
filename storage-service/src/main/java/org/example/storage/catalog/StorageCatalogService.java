package org.example.storage.catalog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageCatalogService {
    private final StorageModelRepository modelRepository;
    private final StorageDetailRepository detailRepository;
    private final StorageCarRepository carRepository;

    public StorageModelJpaEntity saveModel(StorageModelDto dto) {
        StorageModelJpaEntity entity = dto.getId() == null
                ? new StorageModelJpaEntity()
                : findModel(dto.getId());
        entity.setName(dto.getName());
        entity.setBrand(dto.getBrand());
        entity.setBasePrice(dto.getBasePrice());
        entity.setRemoved(false);
        return modelRepository.save(entity);
    }

    public List<StorageModelJpaEntity> findAllModels() {
        return modelRepository.findAllByRemovedFalse();
    }

    public StorageModelJpaEntity findModel(UUID id) {
        return modelRepository.findByIdAndRemovedFalse(id)
                .orElseThrow(() -> new StorageCatalogNotFoundException("Storage model", id));
    }

    public void deleteModel(UUID id) {
        StorageModelJpaEntity entity = findModel(id);
        entity.setRemoved(true);
        modelRepository.save(entity);
    }

    public StorageDetailJpaEntity saveDetail(StorageDetailDto dto) {
        StorageDetailJpaEntity entity = dto.getId() == null
                ? new StorageDetailJpaEntity()
                : findDetail(dto.getId());
        entity.setDetailType(dto.getDetailType());
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setRemoved(false);
        return detailRepository.save(entity);
    }

    public List<StorageDetailJpaEntity> findAllDetails() {
        return detailRepository.findAllByRemovedFalse();
    }

    public StorageDetailJpaEntity findDetail(UUID id) {
        return detailRepository.findByIdAndRemovedFalse(id)
                .orElseThrow(() -> new StorageCatalogNotFoundException("Storage detail", id));
    }

    public void deleteDetail(UUID id) {
        StorageDetailJpaEntity entity = findDetail(id);
        entity.setRemoved(true);
        detailRepository.save(entity);
    }

    public StorageCarJpaEntity saveCar(StorageCarDto dto) {
        findModel(dto.getModelId());
        StorageCarJpaEntity entity = dto.getId() == null
                ? new StorageCarJpaEntity()
                : findCar(dto.getId());
        entity.setModelId(dto.getModelId());
        entity.setColor(dto.getColor());
        entity.setPrice(dto.getPrice());
        entity.setAvailableForTestDrive(dto.isAvailableForTestDrive());
        entity.setRemoved(false);
        return carRepository.save(entity);
    }

    public List<StorageCarJpaEntity> findAllCars() {
        return carRepository.findAllByRemovedFalse();
    }

    public StorageCarJpaEntity findCar(UUID id) {
        return carRepository.findByIdAndRemovedFalse(id)
                .orElseThrow(() -> new StorageCatalogNotFoundException("Storage car", id));
    }

    public void deleteCar(UUID id) {
        StorageCarJpaEntity entity = findCar(id);
        entity.setRemoved(true);
        carRepository.save(entity);
    }
}
