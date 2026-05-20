package org.example.Infrastructure.Repositories.database;

import lombok.RequiredArgsConstructor;
import org.example.exception.NotFoundException.*;
import org.example.lab1.Domain.Entities.Cars.ConfiguredModel;
import org.example.lab1.Domain.Entities.Sales.ConfiguredCarOrder;
import org.example.lab1.Domain.Entities.Details.Detail;
import org.example.lab1.Domain.repository.BaseRepository;
import org.example.persistence.entity.CarModelJpaEntity;
import org.example.persistence.entity.ConfiguredModelJpaEntity;
import org.example.persistence.entity.ConfiguredOrderJpaEntity;
import org.example.persistence.entity.DetailJpaEntity;
import org.example.persistence.repository.CarModelJpaRepository;
import org.example.persistence.repository.ConfiguredCarOrderJpaRepository;
import org.example.persistence.repository.ConfiguredModelJpaRepository;
import org.example.persistence.repository.DetailJpaRepository;
import org.example.persistence.repository.UserJpaRepository;
import org.example.persistence.utils.OrderPersistenceMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConfiguredCarOrderRepositoryAdapter implements BaseRepository<ConfiguredCarOrder, UUID> {
    private final ConfiguredCarOrderJpaRepository configuredCarOrderJpaRepository;
    private final ConfiguredModelJpaRepository configuredModelJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final CarModelJpaRepository carModelJpaRepository;
    private final DetailJpaRepository detailJpaRepository;
    private final OrderPersistenceMapper orderPersistenceMapper;

    @Override
    @Transactional
    public void save(ConfiguredCarOrder order) {
        ConfiguredOrderJpaEntity entity = configuredCarOrderJpaRepository.findById(order.getId())
                .orElseGet(ConfiguredOrderJpaEntity::new);

        entity.setId(order.getId());
        entity.setCustomer(userJpaRepository.findByIdAndRemovedFalse(order.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer with id: " + order.getCustomerId() + " not found")));
        entity.setManager(userJpaRepository.findByIdAndRemovedFalse(order.getManagerId())
                .orElseThrow(() -> new EntityNotFoundException("Manager with id: " + order.getManagerId() + " not found")));
        entity.setConfiguredModel(saveConfiguredModel(order.getConfiguredModel(), entity.getConfiguredModel()));
        entity.setTotalPrice(order.getTotalPrice().value());
        entity.setStatus(order.getStatus());
        entity.setRemoved(false);

        configuredCarOrderJpaRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(UUID uuid) {
        ConfiguredOrderJpaEntity entity = configuredCarOrderJpaRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Order with id: " + uuid + " not found"));
        entity.setRemoved(true);
        configuredCarOrderJpaRepository.save(entity);
    }

    @Override
    public ConfiguredCarOrder findById(UUID uuid) {
        return configuredCarOrderJpaRepository.findByIdAndRemovedFalse(uuid)
                .map(orderPersistenceMapper::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Order with id: " + uuid + " not found"));
    }

    @Override
    public List<ConfiguredCarOrder> findAll() {
        return configuredCarOrderJpaRepository.findAllByRemovedFalse().stream()
                .map(orderPersistenceMapper::toDomain)
                .toList();
    }

    private ConfiguredModelJpaEntity saveConfiguredModel(ConfiguredModel configuredModel, ConfiguredModelJpaEntity existingEntity) {
        ConfiguredModelJpaEntity entity = existingEntity == null ? new ConfiguredModelJpaEntity() : existingEntity;
        CarModelJpaEntity modelEntity = carModelJpaRepository.findByIdAndRemovedFalse(configuredModel.getModel().getId())
                .orElseThrow(() -> new EntityNotFoundException("Model with id: " + configuredModel.getModel().getId() + " not found"));

        entity.setModel(modelEntity);
        entity.setTotalPrice(configuredModel.getTotalPrice().value());
        entity.setEngine(modelEntity.getEngine());
        entity.setTransmission(resolveDetail(configuredModel.getTransmission(), "Transmission"));
        entity.setWheels(resolveDetail(configuredModel.getWheels(), "Wheels"));
        entity.setSteeringWheel(resolveDetail(configuredModel.getSteeringWheel(), "Steering wheel"));
        entity.setInterior(resolveDetail(configuredModel.getInterior(), "Interior"));
        entity.setRemoved(false);

        return configuredModelJpaRepository.save(entity);
    }

    @SuppressWarnings("unchecked")
    private <T extends DetailJpaEntity> T resolveDetail(Detail detail, String typeName) {
        return (T) detailJpaRepository.findByIdAndRemovedFalse(detail.getId())
                .or(() -> detailJpaRepository.findByNameIgnoreCaseAndRemovedFalse(detail.getName()))
                .orElseThrow(() -> new EntityNotFoundException(typeName + " " + detail.getName() + " not found"));
    }
}
