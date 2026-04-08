package org.example.lab1.Domain.Entities.Sales;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.exception.DomainValidationExceptions.*;
import org.example.lab1.Domain.ValueObjects.Price;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class OrderInStock implements IOrder{

    private final UUID id;
    private final UUID customerId;
    private UUID carId;
    private UUID managerId;
    private Price totalPrice;
    private LocalDateTime createdAt;
    private InStockOrderStatus status;

    public void approveByManager(){
        if (status != InStockOrderStatus.CREATED){
            throw new DomainValidationException("Unavailable to move from " + status.toString() + " to " + InStockOrderStatus.APPROVED_BY_MANAGER.toString());
        }
        status = InStockOrderStatus.APPROVED_BY_MANAGER;
    }

    public static OrderInStock create(UUID customerId, UUID managerId, UUID carId, Price totalPrice){
        if(customerId == null){
            throw new DomainValidationException("Customer ID of sale cannot be null");
        }

        return new OrderInStock(
                UUID.randomUUID(),
                customerId,
                carId,
                managerId,
                totalPrice,
                LocalDateTime.now(),
                InStockOrderStatus.CREATED
        );
    }

    @Override
    public void moveToAwaitingPayment(){
        if (status != InStockOrderStatus.APPROVED_BY_MANAGER){
            throw new DomainValidationException("Unavailable to move from " + status.toString() + " to " + InStockOrderStatus.AWAITING_PAYMENT.toString());
        }
        status = InStockOrderStatus.AWAITING_PAYMENT;
    }

    @Override
    public void markPaid() {
        if (status != InStockOrderStatus.AWAITING_PAYMENT){
            throw new DomainValidationException("Unavailable to move from " + status.toString() + " to " + InStockOrderStatus.PAID.toString());
        }
        status = InStockOrderStatus.PAID;
    }

    @Override
    public void markReadyForPickUp() {
        if (status != InStockOrderStatus.PAID){
            throw new DomainValidationException("Unavailable to move from " + status.toString() + " to " + InStockOrderStatus.READY_FOR_PICKUP.toString());
        }
        status = InStockOrderStatus.READY_FOR_PICKUP;
    }

    @Override
    public void complete() {
        if (status != InStockOrderStatus.READY_FOR_PICKUP){
            throw new DomainValidationException("Unavailable to move from " + status.toString() + " to " + InStockOrderStatus.COMPLETED.toString());
        }
        status = InStockOrderStatus.COMPLETED;
    }

    @Override
    public void cancel() {
        if (status == InStockOrderStatus.COMPLETED){
            throw new DomainValidationException("Unavailable to move from " + status.toString() + " to " + InStockOrderStatus.CANCELLED.toString());
        }
        status = InStockOrderStatus.CANCELLED;
    }
}
