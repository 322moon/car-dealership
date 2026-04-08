package org.example.lab1.Domain.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.exception.DomainValidationExceptions.*;
import org.example.lab1.Domain.Entities.Cars.Car;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class TestDrive {
    private final UUID id;
    private final UUID customerId;
    private final UUID carId;
    private final LocalDateTime testDate;
    private boolean isConfirm;

    public static TestDrive create(UUID customerId, UUID carId, LocalDateTime testDate){
        if(customerId == null){
            throw new DomainValidationException("Customer id cannot be null");
        }
        if (carId == null) {
            throw new DomainValidationException("Car id cannot be null");
        }

        return new TestDrive(UUID.randomUUID(), customerId, carId, testDate, false);
    }

}
