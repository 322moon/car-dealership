package org.example.security;

import lombok.RequiredArgsConstructor;
import org.example.lab1.Domain.Services.AdminService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("orderSecurity")
@RequiredArgsConstructor
public class OrderSecurity {
    private final AdminService adminService;
    private final CurrentUser currentUser;

    public boolean isInStockOrderOwner(UUID orderId) {
        return adminService.findInStockOrderById(orderId)
                .getCustomerId()
                .equals(currentUser.id());
    }

    public boolean isConfiguredOrderOwner(UUID orderId) {
        return adminService.findConfiguredOrderById(orderId)
                .getCustomerId()
                .equals(currentUser.id());
    }
}
