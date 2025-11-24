package practical.task.userservice.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import practical.task.userservice.dto.request.userDto.UserCreateDto;
import practical.task.userservice.dto.response.UserResponse;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.cache.type=none"
        }
)
class UserPaginationIntegrationTest {

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

    private String baseUrl;

    @BeforeEach
    void setup() {
        baseUrl = "http://localhost:" + port + "/api/user";
    }

    @Test
    void testGetAllUsersWithPagination() throws Exception{

        //given
        for (int i = 1; i <= 15; i++) {
            UserCreateDto dto = new UserCreateDto(
                    "Name" + i,
                    "Surname" + i,
                    LocalDate.of(1990, 1, i <= 28 ? i : 1),
                    "email" + i + "@example.com"
            );
            restTemplate.postForEntity(baseUrl + "/registration", dto, UserResponse.class);
        }

        ObjectMapper mapper = new ObjectMapper();

        //when
        ResponseEntity<String> page1 = restTemplate.getForEntity(
                baseUrl + "/get/all?page=0&size=10",
                String.class
        );

        JsonNode json1 = mapper.readTree(page1.getBody());

        ResponseEntity<String> page2 = restTemplate.getForEntity(
                baseUrl + "/get/all?page=1&size=10",
                String.class
        );

        JsonNode json2 = mapper.readTree(page2.getBody());

        //then
        assertEquals(HttpStatus.OK, page1.getStatusCode());
        assertEquals(10, json1.get("size").asInt());
        assertEquals(0, json1.get("number").asInt());

        assertEquals(HttpStatus.OK, page2.getStatusCode());
        assertEquals(1, json2.get("number").asInt());
    }
}

