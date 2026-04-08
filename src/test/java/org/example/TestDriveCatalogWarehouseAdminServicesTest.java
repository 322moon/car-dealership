package org.example;

import org.example.Infrastructure.Repositories.InMemory.*;
import org.example.exception.DomainValidationExceptions;
import org.example.exception.NotFoundException;
import org.example.lab1.Domain.Entities.Cars.Car;
import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.CarBrand;
import org.example.lab1.Domain.Entities.Cars.CarCharacteristics.CarColor;
import org.example.lab1.Domain.Entities.Cars.ConfiguredModel;
import org.example.lab1.Domain.Entities.Cars.Model;
import org.example.lab1.Domain.Entities.Cars.ReadyToUseConfigurations.ReadyModels;
import org.example.lab1.Domain.Entities.Details.Detail;
import org.example.lab1.Domain.Entities.Details.Wheels;
import org.example.lab1.Domain.Entities.Sales.ConfiguredCarOrder;
import org.example.lab1.Domain.Entities.Sales.OrderInStock;
import org.example.lab1.Domain.Entities.TestDrive;
import org.example.lab1.Domain.Entities.Users.Customer;
import org.example.lab1.Domain.Entities.Users.Manager;
import org.example.lab1.Domain.Entities.Users.SystemAdmin;
import org.example.lab1.Domain.Entities.Users.User;
import org.example.lab1.Domain.Services.*;
import org.example.lab1.Domain.ValueObjects.Price;
import org.example.lab1.Domain.repository.BaseRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestDriveCatalogWarehouseAdminServicesTest {
    @Test
    void testDriveServiceShouldCreateConfirmAndToggleAvailability() {
        BaseRepository<TestDrive, UUID> testDriveRepo = new TestDriveRepository();
        BaseRepository<Car, UUID> carRepo = new CarRepository();
        BaseRepository<User, UUID> userRepo = new UserRepository();

        User customer = new Customer("customer@mail.ru", "Customer");
        Car car = Car.create(ReadyModels.bmw320i(), CarColor.BLUE, Price.of(3_200_000), true, false);

        userRepo.save(customer);
        carRepo.save(car);

        TestDriveService service = new TestDriveService(testDriveRepo, carRepo, userRepo);
        service.addCarToTestDriveList(car.getId());

        assertTrue(carRepo.findById(car.getId()).isAvailableForTestDrive());

        TestDrive request = service.createRequest(customer.getId(), car.getId(), LocalDateTime.now().plusDays(1));
        assertEquals(1, testDriveRepo.findAll().size());
        assertFalse(request.isConfirm());

        service.confirmRequest(request.getId());
        assertTrue(testDriveRepo.findById(request.getId()).isConfirm());

        service.remoceCarFromTestDriveList(car.getId());
        assertFalse(carRepo.findById(car.getId()).isAvailableForTestDrive());
    }

    @Test
    void testDriveServiceShouldRejectInvalidRequests() {
        BaseRepository<TestDrive, UUID> testDriveRepo = new TestDriveRepository();
        BaseRepository<Car, UUID> carRepo = new CarRepository();
        BaseRepository<User, UUID> userRepo = new UserRepository();

        User manager = new Manager("manager@mail.ru", "Manager");
        Car unavailableCar = Car.create(ReadyModels.bmw320i(), CarColor.WHITE, Price.of(3_200_000), true, false);

        userRepo.save(manager);
        carRepo.save(unavailableCar);

        TestDriveService service = new TestDriveService(testDriveRepo, carRepo, userRepo);

        assertThrows(
                DomainValidationExceptions.DomainValidationException.class,
                () -> service.createRequest(manager.getId(), unavailableCar.getId(), LocalDateTime.now().plusDays(1))
        );
    }

    @Test
    void catalogServiceShouldFilterCarsAndValidateArguments() {
        BaseRepository<Car, UUID> carRepo = new CarRepository();
        CatalogService service = new CatalogService(carRepo);

        Car red320 = Car.create(ReadyModels.bmw320i(), CarColor.RED, Price.of(3_200_000), true, false);
        Car blue330 = Car.create(ReadyModels.bmw330i(), CarColor.BLUE, Price.of(3_850_000), true, false);
        Car hiddenCar = Car.create(ReadyModels.bmwM340i(), CarColor.BLACK, Price.of(4_700_000), false, false);

        carRepo.save(red320);
        carRepo.save(blue330);
        carRepo.save(hiddenCar);

        CarFilter filter = CarFilter.builder()
                .brand(CarBrand.BMW)
                .modelName("BMW 320i")
                .color(CarColor.RED)
                .build();

        List<Car> filtered = service.findAvailableCars(filter);

        assertEquals(2, service.findAvailableCars().size());
        assertEquals(1, filtered.size());
        assertEquals(red320.getId(), filtered.getFirst().getId());

        assertThrows(
                DomainValidationExceptions.DomainValidationException.class,
                () -> service.findAvailableCars(CarFilter.builder().modelName("BMW 320i").build())
        );
    }

    @Test
    void warehouseServiceShouldSupportCrudOperations() {
        BaseRepository<Car, UUID> carRepo = new CarRepository();
        BaseRepository<Detail, UUID> detailRepo = new DetailsRepository();
        WarehouseService service = new WarehouseService(carRepo, detailRepo);

        Car car = Car.create(ReadyModels.bmw320i(), CarColor.GREY, Price.of(3_250_000), true, false);
        Wheels wheels = new Wheels(UUID.randomUUID(), "Warehouse wheels", new java.util.ArrayList<>(), true, Price.of(10_000));

        service.addCar(car);
        service.addDetail(wheels);

        assertEquals(car.getId(), service.findCarById(car.getId()).getId());
        assertEquals(wheels.getId(), service.findDetailById(wheels.getId()).getId());
        assertEquals(1, service.findAllCars().size());
        assertEquals(1, service.findAllDetails().size());

        service.updateCar(car);
        service.updateDetail(wheels);

        assertThrows(DomainValidationExceptions.DomainValidationException.class, () -> service.addCar(null));
        assertThrows(DomainValidationExceptions.DomainValidationException.class, () -> service.addDetail(null));
    }

    @Test
    void adminServiceShouldSupportCrudForAllMainEntities() {
        BaseRepository<User, UUID> userRepo = new UserRepository();
        BaseRepository<Model, UUID> modelRepo = new ModelRepository();
        BaseRepository<Car, UUID> carRepo = new CarRepository();
        BaseRepository<Detail, UUID> detailRepo = new DetailsRepository();
        BaseRepository<OrderInStock, UUID> inStockOrderRepo = new OrderInStockRepository();
        BaseRepository<ConfiguredCarOrder, UUID> configuredOrderRepo = new ConfiguredCarOrderRepository();
        BaseRepository<TestDrive, UUID> testDriveRepo = new TestDriveRepository();

        AdminService service = new AdminService(
                userRepo,
                modelRepo,
                carRepo,
                detailRepo,
                inStockOrderRepo,
                configuredOrderRepo,
                testDriveRepo
        );

        User user = new SystemAdmin("admin@mail.ru", "Admin");
        Model model = ReadyModels.bmw320i();
        Car car = Car.create(model, CarColor.BLACK, Price.of(3_300_000), true, true);
        Wheels detail = new Wheels(UUID.randomUUID(), "Admin wheels", new java.util.ArrayList<>(), true, Price.of(0));
        OrderInStock inStockOrder = OrderInStock.create(UUID.randomUUID(), UUID.randomUUID(), car.getId(), Price.of(3_300_000));
        ConfiguredModel configuredModel = new ConfiguredModel(model);
        configuredModel.setTotalPrice(Price.of(3_400_000));
        ConfiguredCarOrder configuredOrder = ConfiguredCarOrder.create(UUID.randomUUID(), configuredModel, UUID.randomUUID());
        TestDrive testDrive = TestDrive.create(UUID.randomUUID(), car.getId(), LocalDateTime.now().plusDays(2));

        service.saveUser(user);
        service.saveModel(model);
        service.saveCar(car);
        service.saveDetail(detail);
        service.saveInStockOrder(inStockOrder);
        service.saveConfiguredOrder(configuredOrder);
        service.saveTestDrive(testDrive);

        assertEquals(user.getId(), service.findUserById(user.getId()).getId());
        assertEquals(model.getId(), service.findModelById(model.getId()).getId());
        assertEquals(car.getId(), service.findCarById(car.getId()).getId());
        assertEquals(detail.getId(), service.findDetailById(detail.getId()).getId());
        assertEquals(inStockOrder.getId(), service.findInStockOrderById(inStockOrder.getId()).getId());
        assertEquals(configuredOrder.getId(), service.findConfiguredOrderById(configuredOrder.getId()).getId());
        assertEquals(testDrive.getId(), service.findTestDriveById(testDrive.getId()).getId());

        assertEquals(1, service.findAllUsers().size());
        assertEquals(1, service.findAllModels().size());
        assertEquals(1, service.findAllCars().size());
        assertEquals(1, service.findAllDetails().size());
        assertEquals(1, service.findAllInStockOrders().size());
        assertEquals(1, service.findAllConfiguredOrders().size());
        assertEquals(1, service.findAllTestDrives().size());

        service.deleteUser(user.getId());
        service.deleteModel(model.getId());
        service.deleteCar(car.getId());
        service.deleteDetail(detail.getId());
        service.deleteInStockOrder(inStockOrder.getId());
        service.deleteConfiguredOrder(configuredOrder.getId());
        service.deleteTestDrive(testDrive.getId());

        assertThrows(NotFoundException.EntityNotFoundException.class, () -> service.findUserById(user.getId()));
        assertThrows(DomainValidationExceptions.DomainValidationException.class, () -> service.saveUser(null));
    }
}
