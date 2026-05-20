package org.example.Infrastructure.query;

import lombok.RequiredArgsConstructor;
import org.example.integration.outbox.OrderOutboxService;
import org.example.lab1.Domain.Entities.Sales.ConfiguredCarOrder;
import org.example.lab1.Domain.Entities.Sales.OrderInStock;
import org.example.lab1.Domain.Services.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderLifecycleService {
    private final AdminService adminService;
    private final OrderOutboxService orderOutboxService;

    @Transactional
    public OrderInStock markInStockOrderPaid(UUID id) {
        OrderInStock order = adminService.findInStockOrderById(id);
        order.markPaid();
        adminService.saveInStockOrder(order);
        orderOutboxService.saveOrderSentForApproval(order);
        return order;
    }

    @Transactional
    public ConfiguredCarOrder markConfiguredOrderPaid(UUID id) {
        ConfiguredCarOrder order = adminService.findConfiguredOrderById(id);
        order.markPaid();
        adminService.saveConfiguredOrder(order);
        orderOutboxService.saveOrderSentForApproval(order);
        return order;
    }
}
