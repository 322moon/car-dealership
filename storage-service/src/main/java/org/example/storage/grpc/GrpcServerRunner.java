package org.example.storage.grpc;

import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class GrpcServerRunner {
    private final CarAvailabilityGrpcService carAvailabilityGrpcService;

    @Value("${grpc.server.port:9090}")
    private int port;

    private Server server;

    @PostConstruct
    public void start() throws IOException {
        server = NettyServerBuilder.forPort(port)
                .addService(carAvailabilityGrpcService)
                .build()
                .start();
        log.info("StorageService gRPC server started on port {}", port);
    }

    @PreDestroy
    public void stop() {
        if (server != null) {
            server.shutdownNow();
            log.info("StorageService gRPC server stopped");
        }
    }
}
