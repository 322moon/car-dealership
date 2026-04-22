package org.example;

import org.example.Infrastructure.dto.car.CarResponseDto;
import org.example.Infrastructure.dto.inStockOrder.InStockOrderRequestDto;
import org.example.Infrastructure.dto.inStockOrder.InStockOrderResponseDto;
import org.example.Infrastructure.dto.model.ModelResponseDto;
import org.example.Infrastructure.dto.testDrive.TestDriveRequestDto;
import org.example.Infrastructure.dto.testDrive.TestDriveResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BackendIntegrationTest {

    private static final UUID CUSTOMER_ID = UUID.fromString("00000000-0000-0000-0000-000000000101");
    private static final UUID CAR_ID = UUID.fromString("00000000-0000-0000-0000-000000000501");

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("car_dealership_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
        registry.add("spring.liquibase.enabled", () -> "true");
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void liquibaseShouldCreateTablesAndSeedData() {
        Integer usersCount = jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
        Integer modelsCount = jdbcTemplate.queryForObject("select count(*) from models", Integer.class);
        Integer carsCount = jdbcTemplate.queryForObject("select count(*) from cars", Integer.class);

        assertThat(usersCount).isGreaterThanOrEqualTo(4);
        assertThat(modelsCount).isGreaterThanOrEqualTo(3);
        assertThat(carsCount).isGreaterThanOrEqualTo(3);
    }

    @Test
    void carAndModelEndpointsShouldReturnSeedData() {
        ResponseEntity<List<CarResponseDto>> carsResponse = restTemplate.exchange(
                "/api/cars",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        ResponseEntity<List<ModelResponseDto>> modelsResponse = restTemplate.exchange(
                "/api/models",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(carsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(carsResponse.getBody()).isNotNull();
        assertThat(carsResponse.getBody()).extracting(CarResponseDto::getModelName).contains("BMW 320i");

        assertThat(modelsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(modelsResponse.getBody()).isNotNull();
        assertThat(modelsResponse.getBody()).extracting(ModelResponseDto::getName).contains("BMW 320i", "BMW 330i");
    }

    @Test
    void modelSpecificationEndpointShouldFilterByBrand() {
        ResponseEntity<List<ModelResponseDto>> response = restTemplate.exchange(
                "/api/models/filter?brand=BMW",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSizeGreaterThanOrEqualTo(3);
        assertThat(response.getBody()).allSatisfy(model -> assertThat(model.getCarBrand()).isEqualTo("BMW"));
    }

    @Test
    void orderEndpointShouldCreateInStockOrder() {
        InStockOrderRequestDto request = new InStockOrderRequestDto();
        request.setCustomerId(CUSTOMER_ID);
        request.setCarId(CAR_ID);

        ResponseEntity<InStockOrderResponseDto> response = restTemplate.postForEntity(
                "/api/orders/in-stock",
                request,
                InStockOrderResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCustomerId()).isEqualTo(CUSTOMER_ID);
        assertThat(response.getBody().getCarId()).isEqualTo(CAR_ID);
        assertThat(response.getBody().getStatus()).isEqualTo("CREATED");
    }

    @Test
    void testDriveEndpointShouldCreateRequest() {
        TestDriveRequestDto request = new TestDriveRequestDto();
        request.setCustomerId(CUSTOMER_ID);
        request.setCarId(CAR_ID);
        request.setTestDate(Instant.parse("2026-04-23T10:00:00Z"));

        ResponseEntity<TestDriveResponseDto> response = restTemplate.postForEntity(
                "/api/test-drives",
                request,
                TestDriveResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCustomerId()).isEqualTo(CUSTOMER_ID);
        assertThat(response.getBody().getCarId()).isEqualTo(CAR_ID);
        assertThat(response.getBody().isConfirmed()).isFalse();
    }
}
