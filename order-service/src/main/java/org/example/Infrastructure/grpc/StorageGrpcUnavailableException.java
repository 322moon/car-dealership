package org.example.Infrastructure.grpc;

public class StorageGrpcUnavailableException extends RuntimeException {
    public StorageGrpcUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
