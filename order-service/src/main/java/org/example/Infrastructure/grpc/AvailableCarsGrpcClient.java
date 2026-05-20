package org.example.Infrastructure.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.example.Infrastructure.dto.availablecar.AvailableCarResponseDto;
import org.example.grpc.AvailableCarResponse;
import org.example.grpc.CarAvailabilityServiceGrpc;
import org.example.grpc.GetAvailableCarByIdRequest;
import org.example.grpc.GetAvailableCarsRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class AvailableCarsGrpcClient {
    @Value("${grpc.storage.host:localhost}")
    private String host;

    @Value("${grpc.storage.port:9090}")
    private int port;

    @Value("${grpc.storage.timeout-ms:1000}")
    private long timeoutMs;

    private ManagedChannel channel;
    private CarAvailabilityServiceGrpc.CarAvailabilityServiceBlockingStub stub;

    @PostConstruct
    void init() {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        stub = CarAvailabilityServiceGrpc.newBlockingStub(channel);
        log.info("StorageService gRPC client initialized host={} port={} timeoutMs={}", host, port, timeoutMs);
    }

    @PreDestroy
    void shutdown() {
        if (channel != null) {
            channel.shutdownNow();
        }
    }

    public List<AvailableCarResponseDto> findAvailableCars() {
        try {
            log.info("gRPC call: get available cars");
            return stub.withDeadlineAfter(timeoutMs, TimeUnit.MILLISECONDS)
                    .getAvailableCars(GetAvailableCarsRequest.newBuilder().build())
                    .getCarsList().stream()
                    .map(this::toDto)
                    .toList();
        } catch (StatusRuntimeException exception) {
            throw mapException(exception, null);
        }
    }

    public AvailableCarResponseDto findAvailableCarById(UUID id) {
        try {
            log.info("gRPC call: get available car by id={}", id);
            AvailableCarResponse response = stub.withDeadlineAfter(timeoutMs, TimeUnit.MILLISECONDS)
                    .getAvailableCarById(GetAvailableCarByIdRequest.newBuilder()
                            .setId(id.toString())
                            .build());
            return toDto(response);
        } catch (StatusRuntimeException exception) {
            throw mapException(exception, id);
        }
    }

    private RuntimeException mapException(StatusRuntimeException exception, UUID id) {
        Status.Code code = exception.getStatus().getCode();
        if (code == Status.Code.NOT_FOUND && id != null) {
            return new AvailableCarNotFoundException(id);
        }
        if (code == Status.Code.UNAVAILABLE || code == Status.Code.DEADLINE_EXCEEDED) {
            return new StorageGrpcUnavailableException("StorageService is unavailable", exception);
        }
        return new StorageGrpcUnavailableException("StorageService gRPC call failed", exception);
    }

    private AvailableCarResponseDto toDto(AvailableCarResponse response) {
        AvailableCarResponseDto dto = new AvailableCarResponseDto();
        dto.setId(UUID.fromString(response.getId()));
        dto.setModelId(UUID.fromString(response.getModelId()));
        dto.setModelName(response.getModelName());
        dto.setBrand(response.getBrand());
        dto.setColor(response.getColor());
        dto.setPrice(new BigDecimal(response.getPrice()));
        return dto;
    }
}
