package org.example;

import org.example.Infrastructure.Repositories.InMemory.UserRepository;
import org.example.exception.NotFoundException;
import org.example.lab1.Domain.Entities.Cars.ReadyToUseConfigurations.ReadyModels;
import org.example.lab1.Domain.Entities.Details.Wheels;
import org.example.lab1.Domain.Entities.Users.*;
import org.example.lab1.Domain.Result.ErrorResult;
import org.example.lab1.Domain.Result.SuccessResult;
import org.example.lab1.Domain.ValueObjects.Price;
import org.example.lab1.Domain.repository.BaseRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class RepositoriesTest {
    @Test
    void repositoryCrudAndFailuresShouldWork() {
        BaseRepository<User, UUID> repo = new UserRepository();
        User user = User.create("user@mail.ru", "User Name", List.of(Role.CUSTOMER));

        repo.save(user);

        assertEquals(user.getId(), repo.findById(user.getId()).getId());
        assertEquals(1, repo.findAll().size());

        repo.deleteById(user.getId());
        assertEquals(0, repo.findAll().size());

        assertThrows(NotFoundException.EntityNotFoundException.class, () -> repo.findById(user.getId()));
        assertThrows(NotFoundException.EntityNotFoundException.class, () -> repo.deleteById(user.getId()));
        assertThrows(IllegalArgumentException.class, () -> repo.findById(null));
    }

    @Test
    void userFactoriesAndRoleSpecificConstructorsShouldAssignRoles() {
        User generic = User.create("generic@mail.ru", "Generic User", List.of(Role.CUSTOMER));
        Customer customer = new Customer("customer@mail.ru", "Customer User");
        Manager manager = new Manager("manager@mail.ru", "Manager User");
        WarehouseAdmin warehouseAdmin = new WarehouseAdmin("warehouse@mail.ru", "Warehouse User");
        SystemAdmin systemAdmin = new SystemAdmin("admin@mail.ru", "Admin User");

        assertNotNull(generic.getId());
        assertTrue(generic.getRoles().contains(Role.CUSTOMER));
        assertEquals(List.of(Role.CUSTOMER), customer.getRoles());
        assertEquals(List.of(Role.MANAGER), manager.getRoles());
        assertEquals(List.of(Role.WAREHOUSE_ADMIN), warehouseAdmin.getRoles());
        assertEquals(List.of(Role.SYSTEM_ADMIN), systemAdmin.getRoles());
    }

    @Test
    void priceResultAndAppSmokeShouldWork() {
        Price total = Price.of(10).add(Price.of(-3));
        SuccessResult success = new SuccessResult("ok");
        ErrorResult error = new ErrorResult("fail");

        assertEquals(BigDecimal.valueOf(7), total.value());
        assertThrows(IllegalArgumentException.class, () -> new Price(null));
        assertTrue(success.isSuccess());
        assertEquals("ok", success.getMessage());
        assertFalse(error.isSuccess());
        assertEquals("fail", error.getMessage());
    }

    @Test
    void detailCompatibilityShouldUseModelIds() {
        Wheels wheels = new Wheels(
                UUID.randomUUID(),
                "Test wheels",
                new ArrayList<>(),
                true,
                Price.of(0)
        );

        assertFalse(wheels.isCompatibleWith(ReadyModels.bmw320i()));

        wheels.addCompatibleCar(ReadyModels.bmw320i());

        assertTrue(wheels.isCompatibleWith(ReadyModels.bmw320i()));
    }
}
