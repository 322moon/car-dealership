package org.example.persistence.utils;

import org.example.lab1.Domain.Entities.TestDrive;
import org.example.persistence.entity.TestDriveJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TestDrivePersistenceMapper {
    default TestDrive toDomain(TestDriveJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new TestDrive(
                entity.getId(),
                entity.getCustomer().getId(),
                entity.getCar().getId(),
                entity.getTestDate(),
                entity.isConfirm()
        );
    }
}
