package org.example.lab1.Domain.ValueObjects;

import java.math.BigDecimal;

public record Price(BigDecimal value){

    public Price{
        if(value == null){
            throw new IllegalArgumentException("Price cannot be null");
        }
    }

    public static Price of(long value){
        return new Price(BigDecimal.valueOf(value));
    }

    public static Price of(double value){
        return new Price(BigDecimal.valueOf(value));
    }

    public Price add(Price other) {
        return new Price(this.value.add(other.value()));
    }
}
