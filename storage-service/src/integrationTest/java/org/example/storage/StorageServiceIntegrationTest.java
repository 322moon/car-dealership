package org.example.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StorageServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("storage_service_test")
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
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void liquibaseShouldCreateStorageServiceTables() {
        Integer assemblyOrdersCount = jdbcTemplate.queryForObject("select count(*) from assembly_orders", Integer.class);
        Integer stockItemsCount = jdbcTemplate.queryForObject("select count(*) from stock_items", Integer.class);
        Integer carsCount = jdbcTemplate.queryForObject("select count(*) from storage_cars", Integer.class);

        assertThat(assemblyOrdersCount).isZero();
        assertThat(stockItemsCount).isGreaterThanOrEqualTo(1);
        assertThat(carsCount).isGreaterThanOrEqualTo(1);
    }

    @Test
    void storageEndpointShouldRequireAuthentication() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/assembly-orders", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
