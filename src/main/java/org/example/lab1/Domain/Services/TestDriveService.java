package org.example.lab1.Domain.Services;

import lombok.AllArgsConstructor;
import org.example.exception.DomainValidationExceptions.*;
import org.example.lab1.Domain.Entities.Cars.Car;
import org.example.lab1.Domain.Entities.TestDrive;
import org.example.lab1.Domain.Entities.Users.Role;
import org.example.lab1.Domain.Entities.Users.User;
import org.example.lab1.Domain.repository.BaseRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class TestDriveService {
    private final BaseRepository<TestDrive, UUID> testDriveRepo;
    private final BaseRepository<Car, UUID> carRepo;
    private final BaseRepository<User, UUID> userRepo;

    public TestDrive createRequest(UUID customerId, UUID carId, LocalDateTime startAt){
        User customer = userRepo.findById(customerId);
        if(!customer.getRoles().contains(Role.CUSTOMER)){
            throw new DomainValidationException("Only client can create test drive request");
        }

        Car car = carRepo.findById(carId);
        if(!car.isAvailableForTestDrive()){
            throw new DomainValidationException("Car is not available for test drive");
        }

        TestDrive request = TestDrive.create(customerId, carId, startAt);
        testDriveRepo.save(request);
        return request;
    }

    public void confirmRequest(UUID requestId){
        TestDrive request = testDriveRepo.findById(requestId);
        request.setConfirm(true);
        testDriveRepo.save(request);
    }

    public List<TestDrive> findAllRequests(){
        return testDriveRepo.findAll();
    }

    public TestDrive findRequestById(UUID requestId){
        return testDriveRepo.findById(requestId);
    }

    public void addCarToTestDriveList(UUID carId){
        Car car = carRepo.findById(carId);
        Car updated = car.toBuilder()
                .isAvailableForTestDrive(true)
                .build();
        carRepo.save(updated);
    }

    public void remoceCarFromTestDriveList(UUID carId){
        Car car = carRepo.findById(carId);
        Car updated = car.toBuilder()
                .isAvailableForTestDrive(false)
                .build();
        carRepo.save(updated);
    }
}
