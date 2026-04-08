package org.example.lab1.Domain.Entities.Sales;

public enum ConfiguredOrderStatus {
    CREATED,
    APPROVED_BY_WAREHOUSE,
    AWAITING_PAYMENT,
    PAID,
    AWAITING_DELIVERY,
    READY_FOR_PICKUP,
    COMPLETED,
    CANCELLED
}