package org.example.Infrastructure.Repositories.database;

import lombok.RequiredArgsConstructor;
import org.example.exception.NotFoundException.EntityNotFoundException;
import org.example.lab1.Domain.Entities.Cars.Model;
import org.example.lab1.Domain.Entities.Details.Detail;
import org.example.lab1.Domain.Entities.Details.Engine;
import org.example.lab1.Domain.repository.BaseRepository;
import org.example.persistence.entity.CarModelJpaEntity;
import org.example.persistence.entity.DetailJpaEntity;
import org.example.persistence.entity.EngineJpaEntity;
import org.example.persistence.repository.CarModelJpaRepository;
import org.example.persistence.repository.DetailJpaRepository;
import org.example.persistence.utils.CarPersistenceMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ModelRepositoryAdapter implements BaseRepository<Model, UUID> {
    private final CarModelJpaRepository carModelJpaRepository;
    private final DetailJpaRepository detailJpaRepository;
    private final CarPersistenceMapper carPersistenceMapper;

    @Override
    @Transactional
    public void save(Model model) {
        CarModelJpaEntity entity = carModelJpaRepository.findById(model.getId())
                .orElseGet(CarModelJpaEntity::new);

        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setBasePrice(model.getBasePrice().value());
        entity.setCarType(model.getCarType());
        entity.setCarBrand(model.getCarBrand());
        entity.setFuelType(model.getFuelType());
        entity.setDriveType(model.getDriveType());
        entity.setEngine(resolveEngine(model.getEngine(), entity.getEngine()));
        entity.setTransmission(resolveDetail(model.getTransmission()));
        entity.setWheels(resolveDetail(model.getWheels()));
        entity.setSteeringWheel(resolveDetail(model.getSteeringWheel()));
        entity.setInterior(resolveDetail(model.getInterior()));
        entity.setRemoved(false);

        carModelJpaRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        CarModelJpaEntity entity = carModelJpaRepository.findByIdAndRemovedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Model with id: " + id + " not found"));
        entity.setRemoved(true);
        carModelJpaRepository.save(entity);
    }

    @Override
    public Model findById(UUID id) {
        return carModelJpaRepository.findByIdAndRemovedFalse(id)
                .map(carPersistenceMapper::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Model with id: " + id + " not found"));
    }

    @Override
    public List<Model> findAll() {
        return carModelJpaRepository.findAllByRemovedFalse().stream()
                .map(carPersistenceMapper::toDomain)
                .toList();
    }

    @SuppressWarnings("unchecked")
    private <T extends DetailJpaEntity> T resolveDetail(Detail detail) {
        return (T) detailJpaRepository.findByIdAndRemovedFalse(detail.getId())
                .or(() -> detailJpaRepository.findByNameIgnoreCaseAndRemovedFalse(detail.getName()))
                .orElseThrow(() -> new EntityNotFoundException("Detail " + detail.getName() + " not found"));
    }

    private EngineJpaEntity resolveEngine(Engine engine, EngineJpaEntity existingEngine) {
        if (existingEngine != null) {
            return existingEngine;
        }

        Optional<EngineJpaEntity> foundEngine = detailJpaRepository.findAllByRemovedFalse().stream()
                .filter(EngineJpaEntity.class::isInstance)
                .map(EngineJpaEntity.class::cast)
                .filter(entity -> entity.getEngineCapacity() == engine.getEngineCapacity())
                .filter(entity -> entity.getEnginePower() == engine.getEnginePower())
                .findFirst();

        return foundEngine.orElseThrow(() -> new EntityNotFoundException(
                "Engine " + engine.getEngineCapacity() + "/" + engine.getEnginePower() + " not found"
        ));
    }
}
