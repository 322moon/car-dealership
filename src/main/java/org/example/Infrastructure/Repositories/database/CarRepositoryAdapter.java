package org.example.Infrastructure.Repositories.database;

import lombok.RequiredArgsConstructor;
import org.example.exception.NotFoundException;
import org.example.lab1.Domain.Entities.Cars.Car;
import org.example.lab1.Domain.repository.BaseRepository;
import org.example.persistence.entity.CarJpaEntity;
import org.example.persistence.entity.CarModelJpaEntity;
import org.example.persistence.repository.CarJpaRepository;
import org.example.persistence.repository.CarModelJpaRepository;
import org.example.persistence.utils.CarPersistenceMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarRepositoryAdapter implements BaseRepository<Car, UUID> {

    private final CarJpaRepository carJpaRepository;
    private final CarModelJpaRepository carModelJpaRepository;
    private final CarPersistenceMapper carPersistenceMapper;

    @Override
    @Transactional
    public void save(Car car) {
        CarJpaEntity entity = carJpaRepository.findById(car.getId())
                .orElseGet(CarJpaEntity::new);

        CarModelJpaEntity modelEntity = carModelJpaRepository.findByIdAndRemovedFalse(car.getModel().getId())
                .orElseThrow(() -> new NotFoundException.EntityNotFoundException("Model with id " + car.getModel().getId() + " not found"));

        entity.setId(car.getId());
        entity.setModel(modelEntity);
        entity.setColor(car.getColor());
        entity.setPrice(car.getPrice().value());
        entity.setInStock(car.isInStock());
        entity.setAvailableForTestDrive(car.isAvailableForTestDrive());
        entity.setRemoved(false);

        carJpaRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        CarJpaEntity entity = carJpaRepository.findByIdAndRemovedFalse(id)
                .orElseThrow(() -> new NotFoundException.EntityNotFoundException("Car with id " + id + " not found"));

        entity.setRemoved(true);
        carJpaRepository.save(entity);
    }

    @Override
    public Car findById(UUID id) {
        return carJpaRepository.findByIdAndRemovedFalse(id)
                .map(carPersistenceMapper::toDomain)
                .orElseThrow(() -> new NotFoundException.EntityNotFoundException("Car with id " + id + " not found"));
    }

    @Override
    public List<Car> findAll() {
        return carJpaRepository.findAllByRemovedFalse().stream()
                .map(carPersistenceMapper::toDomain)
                .toList();
    }
}
