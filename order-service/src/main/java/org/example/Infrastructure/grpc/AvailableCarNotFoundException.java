package org.example.Infrastructure.grpc;

import java.util.UUID;

public class AvailableCarNotFoundException extends RuntimeException {
    public AvailableCarNotFoundException(UUID id) {
        super("Available car not found: " + id);
    }
}
