package org.example.Infrastructure.Repositories.database;

import lombok.RequiredArgsConstructor;
import org.example.exception.NotFoundException.EntityNotFoundException;
import org.example.lab1.Domain.Entities.TestDrive;
import org.example.lab1.Domain.repository.BaseRepository;
import org.example.persistence.entity.TestDriveJpaEntity;
import org.example.persistence.repository.CarJpaRepository;
import org.example.persistence.repository.TestDriveJpaRepository;
import org.example.persistence.repository.UserJpaRepository;
import org.example.persistence.utils.TestDrivePersistenceMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TestDriveRepositoryAdapter implements BaseRepository<TestDrive, UUID> {
    private final TestDriveJpaRepository testDriveJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final CarJpaRepository carJpaRepository;
    private final TestDrivePersistenceMapper testDrivePersistenceMapper;

    @Override
    @Transactional
    public void save(TestDrive testDrive) {
        TestDriveJpaEntity entity = testDriveJpaRepository.findById(testDrive.getId())
                .orElseGet(TestDriveJpaEntity::new);

        entity.setId(testDrive.getId());
        entity.setCustomer(userJpaRepository.findByIdAndRemovedFalse(testDrive.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer with id: " + testDrive.getCustomerId() + " not found")));
        entity.setCar(carJpaRepository.findByIdAndRemovedFalse(testDrive.getCarId())
                .orElseThrow(() -> new EntityNotFoundException("Car with id: " + testDrive.getCarId() + " not found")));
        entity.setTestDate(testDrive.getTestDate());
        entity.setConfirm(testDrive.isConfirm());
        entity.setRemoved(false);

        testDriveJpaRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        TestDriveJpaEntity entity = testDriveJpaRepository.findByIdAndRemovedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Test drive with id: " + id + " not found"));
        entity.setRemoved(true);
        testDriveJpaRepository.save(entity);
    }

    @Override
    public TestDrive findById(UUID id) {
        return testDriveJpaRepository.findByIdAndRemovedFalse(id)
                .map(testDrivePersistenceMapper::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Test drive with id: " + id + " not found"));
    }

    @Override
    public List<TestDrive> findAll() {
        return testDriveJpaRepository.findAllByRemovedFalse().stream()
                .map(testDrivePersistenceMapper::toDomain)
                .toList();
    }
}
