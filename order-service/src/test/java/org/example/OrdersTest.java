package org.example;

import org.example.Infrastructure.Repositories.InMemory.CarRepository;
import org.example.Infrastructure.Repositories.InMemory.ConfiguredCarOrderRepository;
import org.example.Infrastructure.Repositories.InMemory.OrderInStockRepository;
import org.example.Infrastructure.Repositories.InMemory.UserRepository;
import org.example.exception.DomainValidationExceptions;
import org.example.lab1.Domain.Entities.Cars.Car;
import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.CarColor;
import org.example.lab1.Domain.Entities.Cars.ConfiguredModel;
import org.example.lab1.Domain.Entities.Cars.ReadyToUseConfigurations.ReadyModels;
import org.example.lab1.Domain.Entities.Sales.ConfiguredCarOrder;
import org.example.lab1.Domain.Entities.Sales.ConfiguredOrderStatus;
import org.example.lab1.Domain.Entities.Sales.InStockOrderStatus;
import org.example.lab1.Domain.Entities.Sales.OrderInStock;
import org.example.lab1.Domain.Entities.Users.Manager;
import org.example.lab1.Domain.Entities.Users.SystemAdmin;
import org.example.lab1.Domain.Entities.Users.User;
import org.example.lab1.Domain.Services.OrderService;
import org.example.lab1.Domain.ValueObjects.Price;
import org.example.lab1.Domain.repository.BaseRepository;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrdersTest {
    @Test
    void orderServiceShouldCreateAndSaveInStockOrder() {
        BaseRepository<User, UUID> userRepo = new UserRepository();
        BaseRepository<ConfiguredCarOrder, UUID> configuredOrderRepo = new ConfiguredCarOrderRepository();
        BaseRepository<OrderInStock, UUID> inStockOrderRepo = new OrderInStockRepository();
        BaseRepository<Car, UUID> carRepo = new CarRepository();

        User manager = new Manager("manager@mail.ru", "Manager");
        User customer = new org.example.lab1.Domain.Entities.Users.Customer("customer@mail.ru", "Customer");
        Car car = Car.create(ReadyModels.bmw320i(), CarColor.RED, Price.of(3_200_000), true, false);

        userRepo.save(manager);
        userRepo.save(customer);
        carRepo.save(car);

        OrderService service = new OrderService(userRepo, configuredOrderRepo, inStockOrderRepo, carRepo);
        OrderInStock order = service.createOrderInStock(customer.getId(), car.getId());

        assertEquals(1, inStockOrderRepo.findAll().size());
        assertEquals(customer.getId(), order.getCustomerId());
        assertEquals(manager.getId(), order.getManagerId());
        assertEquals(car.getId(), order.getCarId());
        assertEquals(InStockOrderStatus.CREATED, order.getStatus());
    }

    @Test
    void orderServiceShouldCreateConfiguredOrder() {
        BaseRepository<User, UUID> userRepo = new UserRepository();
        BaseRepository<ConfiguredCarOrder, UUID> configuredOrderRepo = new ConfiguredCarOrderRepository();
        BaseRepository<OrderInStock, UUID> inStockOrderRepo = new OrderInStockRepository();
        BaseRepository<Car, UUID> carRepo = new CarRepository();

        User manager = new Manager("manager2@mail.ru", "Manager");
        User customer = new org.example.lab1.Domain.Entities.Users.Customer("customer2@mail.ru", "Customer");
        ConfiguredModel model = new ConfiguredModel(ReadyModels.bmw330i());
        model.setTotalPrice(Price.of(4_100_000));

        userRepo.save(manager);
        userRepo.save(customer);

        OrderService service = new OrderService(userRepo, configuredOrderRepo, inStockOrderRepo, carRepo);
        ConfiguredCarOrder order = service.createConfiguredOrder(customer.getId(), model);

        assertEquals(1, configuredOrderRepo.findAll().size());
        assertEquals(customer.getId(), order.getCustomerId());
        assertEquals(manager.getId(), order.getManagerId());
        assertEquals(Price.of(4_100_000), order.getTotalPrice());
        assertEquals(ConfiguredOrderStatus.CREATED, order.getStatus());
    }

    @Test
    void orderServiceShouldRejectNonCustomer() {
        BaseRepository<User, UUID> userRepo = new UserRepository();
        BaseRepository<ConfiguredCarOrder, UUID> configuredOrderRepo = new ConfiguredCarOrderRepository();
        BaseRepository<OrderInStock, UUID> inStockOrderRepo = new OrderInStockRepository();
        BaseRepository<Car, UUID> carRepo = new CarRepository();

        User manager = new Manager("manager3@mail.ru", "Manager");
        User admin = new SystemAdmin("admin@mail.ru", "Admin");
        Car car = Car.create(ReadyModels.bmw320i(), CarColor.BLACK, Price.of(3_200_000), true, false);

        userRepo.save(manager);
        userRepo.save(admin);
        carRepo.save(car);

        OrderService service = new OrderService(userRepo, configuredOrderRepo, inStockOrderRepo, carRepo);

        assertThrows(DomainValidationExceptions.DomainValidationException.class, () -> service.createOrderInStock(admin.getId(), car.getId()));
    }

    @Test
    void orderEntitiesShouldFollowHappyPathTransitions() {
        OrderInStock inStockOrder = OrderInStock.create(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Price.of(100));
        inStockOrder.approveByManager();
        inStockOrder.moveToAwaitingPayment();
        inStockOrder.markPaid();
        inStockOrder.markReadyForPickUp();
        inStockOrder.complete();

        ConfiguredModel configuredModel = new ConfiguredModel(ReadyModels.bmw320i());
        configuredModel.setTotalPrice(Price.of(3_500_000));
        ConfiguredCarOrder configuredOrder = ConfiguredCarOrder.create(UUID.randomUUID(), configuredModel, UUID.randomUUID());
        configuredOrder.approveByWarehouse();
        configuredOrder.moveToAwaitingPayment();
        configuredOrder.markPaid();
        configuredOrder.markAwaitingDelivery();
        configuredOrder.markReadyForPickUp();
        configuredOrder.complete();

        assertEquals(InStockOrderStatus.COMPLETED, inStockOrder.getStatus());
        assertEquals(ConfiguredOrderStatus.COMPLETED, configuredOrder.getStatus());
    }

    @Test
    void orderEntitiesShouldRejectInvalidTransitions() {
        OrderInStock inStockOrder = OrderInStock.create(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Price.of(100));
        ConfiguredModel configuredModel = new ConfiguredModel(ReadyModels.bmw320i());
        configuredModel.setTotalPrice(Price.of(3_500_000));
        ConfiguredCarOrder configuredOrder = ConfiguredCarOrder.create(UUID.randomUUID(), configuredModel, UUID.randomUUID());

        assertThrows(DomainValidationExceptions.DomainValidationException.class, inStockOrder::markPaid);
        assertThrows(DomainValidationExceptions.DomainValidationException.class, configuredOrder::markAwaitingDelivery);
    }
}
