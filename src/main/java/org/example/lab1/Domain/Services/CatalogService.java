package org.example.lab1.Domain.Services;

import lombok.AllArgsConstructor;
import org.example.exception.DomainValidationExceptions.*;
import org.example.lab1.Domain.Entities.Cars.Car;
import org.example.lab1.Domain.repository.BaseRepository;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class CatalogService {
    private final BaseRepository<Car, UUID> carRepo;

    public Car findCarById(UUID carId){
        return carRepo.findById(carId);
    }

    public List<Car> findAvailableCars(){
        return carRepo.findAll().stream()
                .filter(Car::isInStock)
                .toList();
    }

    public List<Car> findAvailableCars(CarFilter filter){
        if (filter.getModelName() != null && filter.getBrand() == null){
            throw new DomainValidationException("Model filter can be used only with selected brand");
        }

        return carRepo.findAll().stream()
                .filter(Car::isInStock)
                .filter(car -> filter.getMinPrice() == null || car.getPrice().value().compareTo(filter.getMinPrice()) >= 0)
                .filter(car -> filter.getMaxPrice() == null || car.getPrice().value().compareTo(filter.getMaxPrice()) <= 0)
                .filter(car -> filter.getBrand() == null || car.getModel().getCarBrand() == filter.getBrand())
                .filter(car -> filter.getModelName() == null || car.getModel().getName().equalsIgnoreCase(filter.getModelName()))
                .filter(car -> filter.getCarType() == null || car.getModel().getCarType() == filter.getCarType())
                .filter(car -> filter.getFuelType() == null || car.getModel().getFuelType() == filter.getFuelType())
                .filter(car -> filter.getDriveType() == null || car.getModel().getDriveType() == filter.getDriveType())
                .filter(car -> filter.getTransmissionType() == null || car.getModel().getTransmission().getTransmissionType() == filter.getTransmissionType())
                .filter(car -> filter.getColor() == null || car.getColor() == filter.getColor())
                .filter(car -> filter.getMinEnginePower() == null || car.getModel().getEngine().getEnginePower() >= filter.getMinEnginePower())
                .filter(car -> filter.getMaxEnginePower() == null || car.getModel().getEngine().getEnginePower() <= filter.getMaxEnginePower())
                .filter(car -> filter.getMinEngineCapacity() == null || car.getModel().getEngine().getEngineCapacity() >= filter.getMinEngineCapacity())
                .filter(car -> filter.getMaxEngineCapacity() == null || car.getModel().getEngine().getEngineCapacity() <= filter.getMaxEngineCapacity())
                .toList();
    }
}
