package org.example.storage.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.grpc.AvailableCarResponse;
import org.example.grpc.CarAvailabilityServiceGrpc;
import org.example.grpc.GetAvailableCarByIdRequest;
import org.example.grpc.GetAvailableCarsRequest;
import org.example.grpc.GetAvailableCarsResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarAvailabilityGrpcService extends CarAvailabilityServiceGrpc.CarAvailabilityServiceImplBase {
    private final StorageCarAvailabilityQueryService queryService;

    @Override
    public void getAvailableCars(
            GetAvailableCarsRequest request,
            StreamObserver<GetAvailableCarsResponse> responseObserver
    ) {
        try {
            log.info("gRPC request: get available cars");
            GetAvailableCarsResponse response = GetAvailableCarsResponse.newBuilder()
                    .addAllCars(queryService.findAvailableCars().stream()
                            .map(this::toGrpcResponse)
                            .toList())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (RuntimeException exception) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Cannot load available cars")
                    .withCause(exception)
                    .asRuntimeException());
        }
    }

    @Override
    public void getAvailableCarById(
            GetAvailableCarByIdRequest request,
            StreamObserver<AvailableCarResponse> responseObserver
    ) {
        try {
            log.info("gRPC request: get available car by id={}", request.getId());
            UUID id = UUID.fromString(request.getId());
            queryService.findAvailableCarById(id)
                    .map(this::toGrpcResponse)
                    .ifPresentOrElse(response -> {
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                    }, () -> responseObserver.onError(Status.NOT_FOUND
                            .withDescription("Available car not found: " + request.getId())
                            .asRuntimeException()));
        } catch (IllegalArgumentException exception) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid car id: " + request.getId())
                    .withCause(exception)
                    .asRuntimeException());
        } catch (RuntimeException exception) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Cannot load available car")
                    .withCause(exception)
                    .asRuntimeException());
        }
    }

    private AvailableCarResponse toGrpcResponse(AvailableCarView car) {
        return AvailableCarResponse.newBuilder()
                .setId(car.id().toString())
                .setModelId(car.modelId().toString())
                .setModelName(car.modelName())
                .setBrand(car.brand())
                .setColor(car.color())
                .setPrice(car.price().toPlainString())
                .build();
    }

}
