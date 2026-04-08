package org.example.lab1.Domain.Services;

import lombok.AllArgsConstructor;
import org.example.exception.DomainValidationExceptions.*;
import org.example.lab1.Domain.Entities.Cars.Car;
import org.example.lab1.Domain.Entities.Details.Detail;
import org.example.lab1.Domain.repository.BaseRepository;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class WarehouseService {
    private final BaseRepository<Car, UUID> carRepo;
    private final BaseRepository<Detail, UUID> detailRepo;

    public void addCar(Car car){
        validateNotNull(car, "car");
        carRepo.save(car);
    }

    public void addDetail(Detail detail){
        validateNotNull(detail, "Detail");
        detailRepo.save(detail);
    }

    public Car findCarById(UUID carId){
        return carRepo.findById(carId);
    }

    public Detail findDetailById(UUID detailId) {
        return detailRepo.findById(detailId);
    }

    public List<Car> findAllCars() {
        return carRepo.findAll();
    }

    public List<Detail> findAllDetails() {
        return detailRepo.findAll();
    }

    public void updateCar(Car car){
        validateNotNull(car, "Car");
        carRepo.save(car);
    }

    public void updateDetail(Detail detail) {
        validateNotNull(detail, "Detail");
        detailRepo.save(detail);
    }

    private void validateNotNull(Object value, String entityName) {
        if (value == null) {
            throw new DomainValidationException(entityName + " cannot be null");
        }
    }
}
