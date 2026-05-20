package org.example.lab1.Domain.Entities.Sales;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.exception.DomainValidationExceptions.*;
import org.example.lab1.Domain.Entities.Cars.ConfiguredModel;
import org.example.lab1.Domain.ValueObjects.Price;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ConfiguredCarOrder implements IOrder{

    private final UUID id;
    private final UUID customerId;
    private final ConfiguredModel configuredModel;
    private final UUID managerId;
    private final Price totalPrice;
    private final Instant createdAt;
    private ConfiguredOrderStatus status;

    public void approveByWarehouse(){
        if (status != ConfiguredOrderStatus.CREATED){
            throw new DomainValidationException("Unavailable to move from " + status.toString() + " to " + ConfiguredOrderStatus.APPROVED_BY_WAREHOUSE.toString());
        }
        status = ConfiguredOrderStatus.APPROVED_BY_WAREHOUSE;
    }

    public static ConfiguredCarOrder create(UUID customerId, ConfiguredModel model, UUID managerId){
        if (customerId == null){
            throw new DomainValidationException("Customer ID of sale cannot be null");
        }
        return new ConfiguredCarOrder(
                UUID.randomUUID(),
                customerId,
                model,
                managerId,
                model.getTotalPrice(),
                Instant.now(),
                ConfiguredOrderStatus.CREATED
        );
    }

    public void markAwaitingDelivery(){
        if (status != ConfiguredOrderStatus.PAID){
            throw new DomainValidationException("Unavailable to move from " + status.toString() + " to " + ConfiguredOrderStatus.AWAITING_DELIVERY.toString());
        }
        status = ConfiguredOrderStatus.AWAITING_DELIVERY;
    }

    @Override
    public void moveToAwaitingPayment(){
        if (status != ConfiguredOrderStatus.APPROVED_BY_WAREHOUSE){
            throw new DomainValidationException("Unavailable to move from " + status.toString() + " to " + ConfiguredOrderStatus.AWAITING_PAYMENT.toString());
        }
        status = ConfiguredOrderStatus.AWAITING_PAYMENT;
    }

    @Override
    public void markPaid() {
        if (status != ConfiguredOrderStatus.AWAITING_PAYMENT){
            throw new DomainValidationException("Unavailable to move from " + status.toString() + " to " + ConfiguredOrderStatus.PAID.toString());
        }
        status = ConfiguredOrderStatus.PAID;
    }

    @Override
    public void markReadyForPickUp() {
        if (status != ConfiguredOrderStatus.AWAITING_DELIVERY){
            throw new DomainValidationException("Unavailable to move from " + status.toString() + " to " + ConfiguredOrderStatus.READY_FOR_PICKUP.toString());
        }
        status = ConfiguredOrderStatus.READY_FOR_PICKUP;
    }

    @Override
    public void complete() {
        if (status != ConfiguredOrderStatus.READY_FOR_PICKUP){
            throw new DomainValidationException("Unavailable to move from " + status.toString() + " to " + ConfiguredOrderStatus.COMPLETED.toString());
        }
        status = ConfiguredOrderStatus.COMPLETED;
    }

    @Override
    public void cancel() {
        if (status == ConfiguredOrderStatus.COMPLETED){
            throw new DomainValidationException("Unavailable to move from " + status.toString() + " to " + ConfiguredOrderStatus.CANCELLED.toString());
        }
        status = ConfiguredOrderStatus.CANCELLED;
    }
}
