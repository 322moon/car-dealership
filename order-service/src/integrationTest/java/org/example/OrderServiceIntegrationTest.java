package org.example;

import io.grpc.Server;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import org.example.grpc.AvailableCarResponse;
import org.example.grpc.CarAvailabilityServiceGrpc;
import org.example.grpc.GetAvailableCarByIdRequest;
import org.example.grpc.GetAvailableCarsRequest;
import org.example.grpc.GetAvailableCarsResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderServiceIntegrationTest {
    private static final int FAKE_GRPC_PORT = 19091;
    private static final AtomicReference<FakeGrpcMode> fakeGrpcMode = new AtomicReference<>(FakeGrpcMode.SUCCESS);
    private static Server fakeGrpcServer;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("order_service_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
        registry.add("spring.liquibase.enabled", () -> "true");
        registry.add("spring.kafka.bootstrap-servers", () -> "localhost:19092");
        registry.add("app.outbox.publish-delay-ms", () -> "600000");
        registry.add("grpc.storage.host", () -> "localhost");
        registry.add("grpc.storage.port", () -> String.valueOf(FAKE_GRPC_PORT));
        registry.add("grpc.storage.timeout-ms", () -> "1000");
    }

    @BeforeAll
    static void startFakeGrpcServer() throws IOException {
        fakeGrpcServer = NettyServerBuilder.forPort(FAKE_GRPC_PORT)
                .addService(new FakeCarAvailabilityGrpcService())
                .build()
                .start();
    }

    @AfterAll
    static void stopFakeGrpcServer() {
        if (fakeGrpcServer != null && !fakeGrpcServer.isShutdown()) {
            fakeGrpcServer.shutdownNow();
        }
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void liquibaseShouldCreateOrderServiceTables() {
        Integer ordersCount = jdbcTemplate.queryForObject("select count(*) from orders_in_stock", Integer.class);
        Integer outboxCount = jdbcTemplate.queryForObject("select count(*) from outbox_events", Integer.class);
        Integer processedMessagesCount = jdbcTemplate.queryForObject("select count(*) from processed_messages", Integer.class);

        assertThat(ordersCount).isGreaterThanOrEqualTo(1);
        assertThat(outboxCount).isZero();
        assertThat(processedMessagesCount).isZero();
    }

    @Test
    @Order(2)
    void orderEndpointShouldRequireAuthentication() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/orders/in-stock", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(3)
    void grpcClientEndpointShouldReturnAvailableCars() throws Exception {
        fakeGrpcMode.set(FakeGrpcMode.SUCCESS);

        mockMvc.perform(get("/api/v1/cars")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("00000000-0000-0000-0000-000000000501"))
                .andExpect(jsonPath("$[0].modelName").value("BMW 320i"));
    }

    @Test
    @Order(4)
    void grpcClientEndpointShouldReturnAvailableCarById() throws Exception {
        fakeGrpcMode.set(FakeGrpcMode.SUCCESS);

        mockMvc.perform(get("/api/v1/cars/00000000-0000-0000-0000-000000000501")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("00000000-0000-0000-0000-000000000501"))
                .andExpect(jsonPath("$.modelName").value("BMW 320i"));
    }

    @Test
    @Order(5)
    void grpcClientEndpointShouldReturnEmptyResult() throws Exception {
        fakeGrpcMode.set(FakeGrpcMode.EMPTY);

        mockMvc.perform(get("/api/v1/cars")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_MANAGER"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Order(6)
    void grpcClientEndpointShouldReturnServiceUnavailableOnTimeout() throws Exception {
        fakeGrpcMode.set(FakeGrpcMode.TIMEOUT);

        mockMvc.perform(get("/api/v1/cars")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.code").value("STORAGE_SERVICE_UNAVAILABLE"));
    }

    @Test
    @Order(7)
    void grpcClientEndpointShouldReturnServiceUnavailableWhenStorageIsDown() throws Exception {
        fakeGrpcServer.shutdownNow();
        fakeGrpcServer.awaitTermination(1, TimeUnit.SECONDS);

        mockMvc.perform(get("/api/v1/cars")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.code").value("STORAGE_SERVICE_UNAVAILABLE"));
    }

    enum FakeGrpcMode {
        SUCCESS,
        EMPTY,
        TIMEOUT
    }

    static class FakeCarAvailabilityGrpcService extends CarAvailabilityServiceGrpc.CarAvailabilityServiceImplBase {
        @Override
        public void getAvailableCars(
                GetAvailableCarsRequest request,
                StreamObserver<GetAvailableCarsResponse> responseObserver
        ) {
            if (fakeGrpcMode.get() == FakeGrpcMode.TIMEOUT) {
                sleepPastClientDeadline();
            }
            GetAvailableCarsResponse.Builder response = GetAvailableCarsResponse.newBuilder();
            if (fakeGrpcMode.get() == FakeGrpcMode.SUCCESS) {
                response.addCars(availableCar());
            }
            responseObserver.onNext(response.build());
            responseObserver.onCompleted();
        }

        @Override
        public void getAvailableCarById(
                GetAvailableCarByIdRequest request,
                StreamObserver<AvailableCarResponse> responseObserver
        ) {
            if ("00000000-0000-0000-0000-000000000501".equals(request.getId())) {
                responseObserver.onNext(availableCar());
                responseObserver.onCompleted();
                return;
            }
            responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
        }

        private AvailableCarResponse availableCar() {
            return AvailableCarResponse.newBuilder()
                    .setId("00000000-0000-0000-0000-000000000501")
                    .setModelId("00000000-0000-0000-0000-000000000301")
                    .setModelName("BMW 320i")
                    .setBrand("BMW")
                    .setColor("BLACK")
                    .setPrice("3200000.00")
                    .build();
        }

        private void sleepPastClientDeadline() {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
