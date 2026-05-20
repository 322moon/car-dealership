package org.example.lab1.Domain.Services;

import lombok.AllArgsConstructor;
import org.example.exception.DomainValidationExceptions.*;
import org.example.lab1.Domain.Entities.Cars.Car;
import org.example.lab1.Domain.Entities.Cars.Model;
import org.example.lab1.Domain.Entities.Details.Detail;
import org.example.lab1.Domain.Entities.Sales.ConfiguredCarOrder;
import org.example.lab1.Domain.Entities.Sales.OrderInStock;
import org.example.lab1.Domain.Entities.TestDrive;
import org.example.lab1.Domain.Entities.Users.User;
import org.example.lab1.Domain.repository.BaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AdminService {
    private final BaseRepository<User, UUID> userRepo;
    private final BaseRepository<Model, UUID> modelRepo;
    private final BaseRepository<Car, UUID> carRepo;
    private final BaseRepository<Detail, UUID> detailRepo;
    private final BaseRepository<OrderInStock, UUID> inStockOrderRepo;
    private final BaseRepository<ConfiguredCarOrder, UUID> configuredOrderRepo;
    private final BaseRepository<TestDrive, UUID> testDriveRepo;

    public void saveUser(User user){
        validateNotNull(user, "User");
        userRepo.save(user);
    }

    public User findUserById(UUID userId) {
        return userRepo.findById(userId);
    }

    public List<User> findAllUsers() {
        return userRepo.findAll();
    }

    public void deleteUser(UUID userId) {
        userRepo.deleteById(userId);
    }


    public void saveModel(Model model){
        validateNotNull(model, "Model");
        modelRepo.save(model);
    }

    public Model findModelById(UUID modelId) {
        return modelRepo.findById(modelId);
    }

    public List<Model> findAllModels() {
        return modelRepo.findAll();
    }

    public void deleteModel(UUID modelId) {
        modelRepo.deleteById(modelId);
    }

    public void saveCar(Car car){
        validateNotNull(car, "Car");
        carRepo.save(car);
    }

    public Car findCarById(UUID carId) {
        return carRepo.findById(carId);
    }

    public List<Car> findAllCars() {
        return carRepo.findAll();
    }

    public void deleteCar(UUID carId) {
        carRepo.deleteById(carId);
    }

    public void saveDetail(Detail detail){
        validateNotNull(detail, "Detail");
        detailRepo.save(detail);
    }


    public Detail findDetailById(UUID detailId) {
        return detailRepo.findById(detailId);
    }

    public List<Detail> findAllDetails() {
        return detailRepo.findAll();
    }

    public void deleteDetail(UUID detailId) {
        detailRepo.deleteById(detailId);
    }


    public void saveInStockOrder(OrderInStock order){
        validateNotNull(order, "OrderInStock");
        inStockOrderRepo.save(order);
    }

    public OrderInStock findInStockOrderById(UUID orderId) {
        return inStockOrderRepo.findById(orderId);
    }

    public List<OrderInStock> findAllInStockOrders() {
        return inStockOrderRepo.findAll();
    }

    public void deleteInStockOrder(UUID orderId) {
        inStockOrderRepo.deleteById(orderId);
    }

    public void saveConfiguredOrder(ConfiguredCarOrder order) {
        validateNotNull(order, "ConfiguredCarOrder");
        configuredOrderRepo.save(order);
    }

    public ConfiguredCarOrder findConfiguredOrderById(UUID orderId) {
        return configuredOrderRepo.findById(orderId);
    }

    public List<ConfiguredCarOrder> findAllConfiguredOrders() {
        return configuredOrderRepo.findAll();
    }

    public void deleteConfiguredOrder(UUID orderId) {
        configuredOrderRepo.deleteById(orderId);
    }

    public void saveTestDrive(TestDrive testDrive){
        validateNotNull(testDrive, "TestDrive");
        testDriveRepo.save(testDrive);
    }

    public TestDrive findTestDriveById(UUID testDriveId) {
        return testDriveRepo.findById(testDriveId);
    }

    public List<TestDrive> findAllTestDrives() {
        return testDriveRepo.findAll();
    }

    public void deleteTestDrive(UUID testDriveId) {
        testDriveRepo.deleteById(testDriveId);
    }

    private void validateNotNull(Object value, String entityName) {
        if (value == null) {
            throw new DomainValidationException(entityName + " cannot be null");
        }
    }
}
