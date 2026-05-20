package org.example.persistence.utils;

import org.example.lab1.Domain.Entities.Cars.ConfiguredModel;
import org.example.lab1.Domain.Entities.Sales.ConfiguredCarOrder;
import org.example.lab1.Domain.Entities.Sales.OrderInStock;
import org.example.lab1.Domain.ValueObjects.Price;
import org.example.persistence.entity.ConfiguredModelJpaEntity;
import org.example.persistence.entity.ConfiguredOrderJpaEntity;
import org.example.persistence.entity.InStockOrderJpaEntity;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class OrderPersistenceMapper {

    @Autowired
    protected CarPersistenceMapper carPersistenceMapper;

    public OrderInStock toDomain(InStockOrderJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new OrderInStock(
                entity.getId(),
                entity.getCustomer().getId(),
                entity.getCar().getId(),
                entity.getManager().getId(),
                new Price(entity.getTotalPrice()),
                entity.getCreatedAt(),
                entity.getStatus()
        );
    }

    public ConfiguredCarOrder toDomain(ConfiguredOrderJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new ConfiguredCarOrder(
                entity.getId(),
                entity.getCustomer().getId(),
                toDomain(entity.getConfiguredModel()),
                entity.getManager().getId(),
                new Price(entity.getTotalPrice()),
                entity.getCreatedAt(),
                entity.getStatus()
        );
    }

    public ConfiguredModel toDomain(ConfiguredModelJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        ConfiguredModel configuredModel = new ConfiguredModel(carPersistenceMapper.toDomain(entity.getModel()));
        configuredModel.setTotalPrice(new Price(entity.getTotalPrice()));
        configuredModel.setTransmission(carPersistenceMapper.toDomain(entity.getTransmission()));
        configuredModel.setWheels(carPersistenceMapper.toDomain(entity.getWheels()));
        configuredModel.setSteeringWheel(carPersistenceMapper.toDomain(entity.getSteeringWheel()));
        configuredModel.setInterior(carPersistenceMapper.toDomain(entity.getInterior()));
        return configuredModel;
    }
}
