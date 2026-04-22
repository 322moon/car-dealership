package org.example.Infrastructure.Repositories.database;

import lombok.RequiredArgsConstructor;
import org.example.exception.NotFoundException.EntityNotFoundException;
import org.example.lab1.Domain.Entities.Sales.OrderInStock;
import org.example.lab1.Domain.repository.BaseRepository;
import org.example.persistence.entity.InStockOrderJpaEntity;
import org.example.persistence.repository.CarJpaRepository;
import org.example.persistence.repository.InStockOrderJpaRepository;
import org.example.persistence.repository.UserJpaRepository;
import org.example.persistence.utils.OrderPersistenceMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InStockOrderRepositoryAdapter implements BaseRepository<OrderInStock, UUID> {
    private final InStockOrderJpaRepository inStockOrderJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final CarJpaRepository carJpaRepository;
    private final OrderPersistenceMapper orderPersistenceMapper;

    @Override
    @Transactional
    public void save(OrderInStock order) {
        InStockOrderJpaEntity entity = inStockOrderJpaRepository.findById(order.getId())
                .orElseGet(InStockOrderJpaEntity::new);

        entity.setId(order.getId());
        entity.setCustomer(userJpaRepository.findByIdAndRemovedFalse(order.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer with id: " + order.getCustomerId() + " not found")));
        entity.setManager(userJpaRepository.findByIdAndRemovedFalse(order.getManagerId())
                .orElseThrow(() -> new EntityNotFoundException("Manager with id: " + order.getManagerId() + " not found")));
        entity.setCar(carJpaRepository.findByIdAndRemovedFalse(order.getCarId())
                .orElseThrow(() -> new EntityNotFoundException("Car with id: " + order.getCarId() + " not found")));
        entity.setTotalPrice(order.getTotalPrice().value());
        entity.setStatus(order.getStatus());
        entity.setRemoved(false);

        inStockOrderJpaRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        InStockOrderJpaEntity entity = inStockOrderJpaRepository.findByIdAndRemovedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with id: " + id + " not found"));
        entity.setRemoved(true);
        inStockOrderJpaRepository.save(entity);
    }

    @Override
    public OrderInStock findById(UUID id) {
        return inStockOrderJpaRepository.findByIdAndRemovedFalse(id)
                .map(orderPersistenceMapper::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Order with id: " + id + " not found"));
    }

    @Override
    public List<OrderInStock> findAll() {
        return inStockOrderJpaRepository.findAllByRemovedFalse().stream()
                .map(orderPersistenceMapper::toDomain)
                .toList();
    }
}
