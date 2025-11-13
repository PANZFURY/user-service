package practical.task.userservice.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import practical.task.userservice.dto.request.paymentCardDto.CreatePaymentCardDto;
import practical.task.userservice.dto.request.paymentCardDto.UpdatePaymentCardDto;
import practical.task.userservice.dto.request.userDto.UserCreateDto;
import practical.task.userservice.dto.response.PaymentCardResponse;
import practical.task.userservice.dto.response.UserResponse;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentCardControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("userdb")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String userUrl;
    private String cardUrl;

    @BeforeEach
    void setup() {
        userUrl = "http://localhost:" + port + "/api/user";
        cardUrl = "http://localhost:" + port + "/api/card";
    }

    @Test
    void testFullPaymentCardFlow() {
        UserCreateDto createUserDto = new UserCreateDto(
                "Alice",
                "Smith",
                LocalDate.of(1995, 3, 15),
                "alice.smith@example.com"
        );

        ResponseEntity<UserResponse> userResp = restTemplate.postForEntity(
                userUrl + "/registration", createUserDto, UserResponse.class
        );

        assertEquals(HttpStatus.CREATED, userResp.getStatusCode());
        assertNotNull(userResp.getBody());
        Long userId = userResp.getBody().id();

        CreatePaymentCardDto createCardDto = new CreatePaymentCardDto(
                userId,
                "1234-5678-9999-1111",
                LocalDate.of(2030, 12, 31)
        );

        ResponseEntity<PaymentCardResponse> createCardResp = restTemplate.postForEntity(
                cardUrl + "/registration", createCardDto, PaymentCardResponse.class
        );

        assertEquals(HttpStatus.CREATED, createCardResp.getStatusCode());
        assertNotNull(createCardResp.getBody());
        Long cardId = createCardResp.getBody().id();

        ResponseEntity<PaymentCardResponse> getResp =
                restTemplate.getForEntity(cardUrl + "/get/" + cardId, PaymentCardResponse.class);

        assertEquals(HttpStatus.OK, getResp.getStatusCode());
        assertEquals("1234-5678-9999-1111", getResp.getBody().number());

        UpdatePaymentCardDto updateDto = new UpdatePaymentCardDto(
                null,
                "5555-2222-1111-9999",
                null,
                null
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UpdatePaymentCardDto> updateRequest = new HttpEntity<>(updateDto, headers);

        ResponseEntity<PaymentCardResponse> updateResp = restTemplate.exchange(
                cardUrl + "/update/" + cardId,
                HttpMethod.PATCH,
                updateRequest,
                PaymentCardResponse.class
        );

        assertEquals(HttpStatus.OK, updateResp.getStatusCode());
        assertEquals("5555-2222-1111-9999", updateResp.getBody().number());

        ResponseEntity<Void> deleteResp = restTemplate.exchange(
                cardUrl + "/delete/" + cardId,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, deleteResp.getStatusCode());

        ResponseEntity<PaymentCardResponse> afterDelete =
                restTemplate.getForEntity(cardUrl + "/get/" + cardId, PaymentCardResponse.class);

        assertEquals(HttpStatus.OK, afterDelete.getStatusCode());
    }
}
