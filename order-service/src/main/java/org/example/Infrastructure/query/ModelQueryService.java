package org.example.Infrastructure.query;

import lombok.RequiredArgsConstructor;
import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.CarBrand;
import org.example.lab1.Domain.Entities.Cars.Model;
import org.example.persistence.repository.CarModelJpaRepository;
import org.example.persistence.specification.CarModelSpecifications;
import org.example.persistence.utils.CarPersistenceMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ModelQueryService {
    private final CarModelJpaRepository carModelJpaRepository;
    private final CarPersistenceMapper carPersistenceMapper;

    public List<Model> findByFilter(
            CarBrand brand,
            UUID engineId,
            UUID transmissionId,
            UUID wheelsId,
            UUID steeringWheelId,
            UUID interiorId
    ) {
        var specification = CarModelSpecifications.notRemoved()
                .and(CarModelSpecifications.hasBrand(brand))
                .and(CarModelSpecifications.hasEngine(engineId))
                .and(CarModelSpecifications.hasTransmission(transmissionId))
                .and(CarModelSpecifications.hasWheels(wheelsId))
                .and(CarModelSpecifications.hasSteeringWheel(steeringWheelId))
                .and(CarModelSpecifications.hasInterior(interiorId));

        return carModelJpaRepository.findAll(specification).stream()
                .map(carPersistenceMapper::toDomain)
                .toList();
    }
}
