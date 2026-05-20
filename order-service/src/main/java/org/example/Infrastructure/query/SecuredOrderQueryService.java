package org.example.Infrastructure.query;

import lombok.RequiredArgsConstructor;
import org.example.lab1.Domain.Entities.Sales.ConfiguredCarOrder;
import org.example.lab1.Domain.Entities.Sales.OrderInStock;
import org.example.lab1.Domain.Services.AdminService;
import org.example.security.CurrentUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecuredOrderQueryService {
    private final AdminService adminService;
    private final CurrentUser currentUser;

    public List<OrderInStock> findVisibleInStockOrders() {
        if (canSeeAllOrders()) {
            return adminService.findAllInStockOrders();
        }

        return adminService.findAllInStockOrders().stream()
                .filter(order -> order.getCustomerId().equals(currentUser.id()))
                .toList();
    }

    public List<ConfiguredCarOrder> findVisibleConfiguredOrders() {
        if (canSeeAllOrders()) {
            return adminService.findAllConfiguredOrders();
        }

        return adminService.findAllConfiguredOrders().stream()
                .filter(order -> order.getCustomerId().equals(currentUser.id()))
                .toList();
    }

    private boolean canSeeAllOrders() {
        return currentUser.hasRole("MANAGER") || currentUser.hasRole("ADMIN");
    }
}
