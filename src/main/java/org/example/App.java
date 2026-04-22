package org.example;

import org.example.Infrastructure.Repositories.InMemory.*;
import org.example.lab1.Domain.Entities.Cars.Car;
import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.CarColor;
import org.example.lab1.Domain.Entities.Cars.ConfiguredModel;
import org.example.lab1.Domain.Entities.Cars.Model;
import org.example.lab1.Domain.Entities.Cars.ReadyToUseConfigurations.ReadyModels;
import org.example.lab1.Domain.Entities.Cars.ReadyToUseConfigurations.ReadyOptions;
import org.example.lab1.Domain.Entities.Details.Detail;
import org.example.lab1.Domain.Entities.Sales.ConfiguredCarOrder;
import org.example.lab1.Domain.Entities.Sales.OrderInStock;
import org.example.lab1.Domain.Entities.TestDrive;
import org.example.lab1.Domain.Entities.Users.Customer;
import org.example.lab1.Domain.Entities.Users.Manager;
import org.example.lab1.Domain.Entities.Users.SystemAdmin;
import org.example.lab1.Domain.Entities.Users.User;
import org.example.lab1.Domain.Services.CatalogService;
import org.example.lab1.Domain.Services.OrderService;
import org.example.lab1.Domain.Services.TestDriveService;
import org.example.lab1.Domain.ValueObjects.Price;
import org.example.lab1.Domain.configurator.Configurator;
import org.example.lab1.Domain.repository.BaseRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;


public final class App {
    private App(){
    }

    public static void main(String[] args){
        BaseRepository<User, UUID> userRepo = new UserRepository();
        BaseRepository<Car, UUID> carRepo = new CarRepository();
        BaseRepository<ConfiguredCarOrder, UUID> configuredOrderRepo = new ConfiguredCarOrderRepository();
        BaseRepository<OrderInStock, UUID> inStockOrderRepo = new OrderInStockRepository();
        BaseRepository<TestDrive, UUID> testDriveRepo = new TestDriveRepository();
        BaseRepository<Detail, UUID> detailRepo = new DetailsRepository();
        BaseRepository<Model, UUID> modelRepo = new ModelRepository();

        User customer = new Customer("pres_customer@mail.ru", "Petr Vasilich");
        User manager = new Manager("manager@gmail.com", "manager");
        User admin = new SystemAdmin("sysadmin@gmail.com", "system admin");

        userRepo.save(customer);
        userRepo.save(manager);
        userRepo.save(admin);

        List<Model> models = ReadyModels.bmw3Series();
        models.forEach(modelRepo::save);

        ReadyOptions.inStockOptions().getWheels().forEach(detailRepo::save);
        ReadyOptions.inStockOptions().getSteeringWheels().forEach(detailRepo::save);
        ReadyOptions.inStockOptions().getTransmissions().forEach(detailRepo::save);
        ReadyOptions.inStockOptions().getTransmissions().forEach(detailRepo::save);

        Car car320 = Car.create(ReadyModels.bmw320i(), CarColor.BLACK, Price.of(3_200_000), true, true);
        Car car330 = Car.create(ReadyModels.bmw330i(), CarColor.WHITE, Price.of(3_850_000), true, false);

        carRepo.save(car320);
        carRepo.save(car330);

        CatalogService catalogService = new CatalogService(carRepo);
        OrderService orderService = new OrderService(userRepo, configuredOrderRepo, inStockOrderRepo, carRepo);
        TestDriveService testDriveService = new TestDriveService(testDriveRepo, carRepo, userRepo);

        System.out.println("=== Available cars ===");
        catalogService.findAvailableCars().forEach(car ->
                System.out.println(car.getModel().getName() + " | " + car.getColor() + " | " + car.getPrice().value())
        );

        Configurator configurator = new Configurator(ReadyModels.bmw320i());
        ConfiguredModel configuredModel = configurator.configure(
                ReadyOptions.findWheelsByName("18'' Aero"),
                ReadyOptions.findSteeringWheelByName("M-Sport Heated"),
                null,
                ReadyOptions.findInteriorByName("Leather Dakota")
        );

        System.out.println("\n=== Configured model ===");
        System.out.println(configuredModel.getModel().getName() + " | " + configuredModel.getTotalPrice().value());

        OrderInStock inStockOrder = orderService.createOrderInStock(customer.getId(), car320.getId());
        System.out.println("\n=== In-stock order ===");
        System.out.println(inStockOrder.getId() + " | " + inStockOrder.getStatus());

        ConfiguredCarOrder configuredOrder = orderService.createConfiguredOrder(customer.getId(), configuredModel);
        System.out.println("\n=== Configured order ===");
        System.out.println(configuredOrder.getId() + " | " + configuredOrder.getStatus());

        TestDrive request = testDriveService.createRequest(customer.getId(), car320.getId(), Instant.now().plus(1, ChronoUnit.DAYS));
        System.out.println("\n=== Test drive request ===");
        System.out.println(request.getId() + " | " + request.getTestDate());
    }
}
