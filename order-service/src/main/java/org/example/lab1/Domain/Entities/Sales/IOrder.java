package org.example.lab1.Domain.Entities.Sales;

public interface IOrder {
    void markPaid();
    void moveToAwaitingPayment();
    void markReadyForPickUp();
    void complete();
    void cancel();
}
