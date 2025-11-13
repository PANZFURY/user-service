package practical.task.userservice.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import practical.task.userservice.dto.request.paymentCardDto.CreatePaymentCardDto;
import practical.task.userservice.dto.request.userDto.UserCreateDto;
import practical.task.userservice.dto.response.PaymentCardResponse;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentCardPaginationIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("userdb")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;

    private String userUrl;
    private String cardUrl;
    private Long userId;

    @BeforeEach
    void setup() {
        userUrl = "http://localhost:" + port + "/api/user";
        cardUrl = "http://localhost:" + port + "/api/card";

        UserCreateDto dto = new UserCreateDto(
                "John", "Doe", LocalDate.of(1990, 1, 1), "john@example.com"
        );
        ResponseEntity<practical.task.userservice.dto.response.UserResponse> response =
                restTemplate.postForEntity(userUrl + "/registration", dto, practical.task.userservice.dto.response.UserResponse.class);

        userId = response.getBody().id();
    }

    @Test
    void testGetAllPaymentCardsWithPagination() {
        for (int i = 1; i <= 12; i++) {
            CreatePaymentCardDto cardDto = new CreatePaymentCardDto(
                    userId,
                    "1111-2222-3333-" + String.format("%04d", i),
                    LocalDate.of(2030, 1, 1)
            );

            restTemplate.postForEntity(cardUrl + "/registration", cardDto, PaymentCardResponse.class);
        }

        ResponseEntity<String> page1 = restTemplate.getForEntity(
                cardUrl + "/get/all?page=0&size=10",
                String.class
        );

        assertEquals(HttpStatus.OK, page1.getStatusCode());
        assertTrue(page1.getBody().contains("\"size\":10"));
        assertTrue(page1.getBody().contains("\"number\":0"));

        ResponseEntity<String> page2 = restTemplate.getForEntity(
                cardUrl + "/get/all?page=1&size=10",
                String.class
        );

        assertEquals(HttpStatus.OK, page2.getStatusCode());
        assertTrue(page2.getBody().contains("\"number\":1"));
    }
}

