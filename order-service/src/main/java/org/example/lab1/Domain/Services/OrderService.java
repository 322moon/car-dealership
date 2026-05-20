package org.example.lab1.Domain.Services;

import lombok.AllArgsConstructor;
import org.example.exception.DomainValidationExceptions;
import org.example.exception.NotFoundException.*;
import org.example.lab1.Domain.Entities.Cars.Car;
import org.example.lab1.Domain.Entities.Cars.ConfiguredModel;
import org.example.lab1.Domain.Entities.Sales.*;
import org.example.lab1.Domain.Entities.Users.Role;
import org.example.lab1.Domain.Entities.Users.User;
import org.example.lab1.Domain.Result.Result;
import org.example.lab1.Domain.ValueObjects.Price;
import org.example.lab1.Domain.repository.BaseRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderService {
    private BaseRepository<User, UUID> userRepo;
    private BaseRepository<ConfiguredCarOrder, UUID> configuredOrderRepo;
    private BaseRepository<OrderInStock, UUID> inStockOrderRepo;
    private BaseRepository<Car, UUID> carRepo;

    public OrderInStock createOrderInStock(UUID customerId, UUID carId){
        User manager = userRepo.findAll().stream()
                .filter(m->m.getRoles().contains(Role.MANAGER))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Cannot find manager for order"));

        User customer = userRepo.findById(customerId);
        if (!customer.getRoles().contains(Role.CUSTOMER)){
            throw new DomainValidationExceptions.DomainValidationException("Role validation error");
        }

        Price totalPrice = carRepo.findById(carId).getPrice();

        OrderInStock order = OrderInStock.create(customerId, manager.getId(), carId, totalPrice);
        inStockOrderRepo.save(order);

        return order;
    }

    public ConfiguredCarOrder createConfiguredOrder(UUID customerId, ConfiguredModel model){
        User manager = userRepo.findAll().stream()
                .filter(m->m.getRoles().contains(Role.MANAGER))
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException("Cannot find manager for order"));

        User customer = userRepo.findById(customerId);
        if (!customer.getRoles().contains(Role.CUSTOMER)){
            throw new DomainValidationExceptions.DomainValidationException("Role validation error");
        }

        ConfiguredCarOrder order = ConfiguredCarOrder.create(customerId, model, manager.getId());
        configuredOrderRepo.save(order);
        return order;
    }
}
