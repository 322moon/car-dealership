package org.example.Infrastructure.Repositories.database;

import lombok.RequiredArgsConstructor;
import org.example.exception.NotFoundException.EntityNotFoundException;
import org.example.lab1.Domain.Entities.Details.Detail;
import org.example.lab1.Domain.Entities.Details.Interior;
import org.example.lab1.Domain.Entities.Details.SteeringWheel;
import org.example.lab1.Domain.Entities.Details.Transmission;
import org.example.lab1.Domain.Entities.Details.Wheels;
import org.example.lab1.Domain.repository.BaseRepository;
import org.example.persistence.entity.DetailJpaEntity;
import org.example.persistence.entity.InteriorJpaEntity;
import org.example.persistence.entity.SteeringWheelJpaEntity;
import org.example.persistence.entity.TransmissionJpaEntity;
import org.example.persistence.entity.WheelsJpaEntity;
import org.example.persistence.repository.CarModelJpaRepository;
import org.example.persistence.repository.DetailJpaRepository;
import org.example.persistence.utils.CarPersistenceMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DetailRepositoryAdapter implements BaseRepository<Detail, UUID> {
    private final DetailJpaRepository detailJpaRepository;
    private final CarModelJpaRepository carModelJpaRepository;
    private final CarPersistenceMapper carPersistenceMapper;

    @Override
    @Transactional
    public void save(Detail detail) {
        DetailJpaEntity entity = detailJpaRepository.findById(detail.getId())
                .orElseGet(() -> newEntity(detail));

        entity.setId(detail.getId());
        entity.setName(detail.getName());
        entity.setPrice(detail.getPrice().value());
        entity.setInStock(detail.isInStock());
        entity.setCompatibleModels(new HashSet<>(carModelJpaRepository.findAllById(detail.getCompatibleModels())));
        entity.setRemoved(false);

        if (entity instanceof TransmissionJpaEntity transmissionEntity && detail instanceof Transmission transmission) {
            transmissionEntity.setTransmissionType(transmission.getTransmissionType());
        }

        detailJpaRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        DetailJpaEntity entity = detailJpaRepository.findByIdAndRemovedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Detail with id: " + id + " not found"));
        entity.setRemoved(true);
        detailJpaRepository.save(entity);
    }

    @Override
    public Detail findById(UUID id) {
        return detailJpaRepository.findByIdAndRemovedFalse(id)
                .map(carPersistenceMapper::detailToDomain)
                .orElseThrow(() -> new EntityNotFoundException("Detail with id: " + id + " not found"));
    }

    @Override
    public List<Detail> findAll() {
        return detailJpaRepository.findAllByRemovedFalse().stream()
                .filter(entity -> !(entity instanceof org.example.persistence.entity.EngineJpaEntity))
                .map(carPersistenceMapper::detailToDomain)
                .toList();
    }

    private DetailJpaEntity newEntity(Detail detail) {
        if (detail instanceof Transmission) {
            return new TransmissionJpaEntity();
        }
        if (detail instanceof Wheels) {
            return new WheelsJpaEntity();
        }
        if (detail instanceof SteeringWheel) {
            return new SteeringWheelJpaEntity();
        }
        if (detail instanceof Interior) {
            return new InteriorJpaEntity();
        }

        throw new IllegalArgumentException("Unsupported detail type: " + detail.getClass().getSimpleName());
    }
}
