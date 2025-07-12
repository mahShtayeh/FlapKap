package com.flapkap.vendingmachine.controller;

import com.flapkap.vendingmachine.model.UserRole;
import com.flapkap.vendingmachine.web.RestResponse;
import com.flapkap.vendingmachine.web.request.RegistrationRequest;
import com.flapkap.vendingmachine.web.response.RegistrationResponse;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for the AuthController. This class verifies the behavior and
 * correctness of AuthController endpoints by simulating requests and analyzing
 * responses within a controlled testing environment.
 */
@NoArgsConstructor
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerTest {
    /**
     * Test server random port
     */
    @LocalServerPort
    private int port;

    /**
     * A final instance of {@link RestTemplate} used for sending HTTP requests during
     * integration tests for the AccountController.
     */
    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * A constant representing the base URL for the test server during integration tests.
     */
    private static final String BASE_URL = "http://localhost";

    /**
     * Represents the API endpoint URL for user registration.
     */
    private static final String REGISTER_URL = "/api/v1/users";

    /**
     * A constant representing a predefined email address primarily used for testing
     * purposes in the integration tests of the {@code AuthControllerTest} class.
     * This value ensures consistency and avoids repetition in test cases where
     * a valid username is required.
     */
    private static final String BUYER_USERNAME = "jack.smith@flapkap.com";

    /**
     * A constant representing a predefined balance value, primarily used
     * within integration tests for validation or simulation purposes.
     * This value is immutable and signifies a fixed amount to ensure consistency
     * across tests that involve monetary computations.
     */
    private static final BigDecimal BALANCE = BigDecimal.valueOf(1000.00);

    /**
     * A constant representing a pre-defined password value, likely used for testing purposes
     * within the {@code UserServiceTest}. This password is stored as a hashed string,
     * emphasizing the importance of security when dealing with sensitive information.
     * SHA256 Hashed value of: <PASSWORD>.
     */
    private static final String HASHED_PASSWORD = "73e460e77a656f17dfe522a7571e2671ead0abf36c4184ef3dfba2d4affe8702";

    /**
     * A unique identifier for a user in the test context.
     * This field is used to store and reference the `UUID` for a user,
     * typically after performing registration or similar operations
     * within the testing framework.
     */
    private static UUID userId;

    /**
     * Tests the registration of a new buyer user via the authentication controller.
     * This test validates the behavior when a valid registration request is sent
     * to the registration endpoint. It ensures the HTTP 201 (Created) status
     * is returned and the response contains a valid payload including a unique user ID.
     */
    @Test
    @Order(1)
    void registerBuyer_withValidRequest_shouldReturn201() {
        final RegistrationRequest accountCreationRequest = RegistrationRequest.builder()
                .username(BUYER_USERNAME)
                .password(HASHED_PASSWORD)
                .deposit(BALANCE)
                .role(UserRole.BUYER)
                .build();

        final ResponseEntity<RestResponse<RegistrationResponse>> response = restTemplate.exchange(
                String.format("%s:%d%s", BASE_URL, port, REGISTER_URL),
                HttpMethod.POST,
                new HttpEntity<>(accountCreationRequest),
                new ParameterizedTypeReference<>() {
                });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        final RestResponse<RegistrationResponse> responseBody = response.getBody();
        assertThat(responseBody)
                .isNotNull()
                .satisfies(body -> {
                    assertThat(body.getPayload()).isNotNull();
                    assertThat(body.getPayload().userId()).isNotNull();
                });

        userId = responseBody.getPayload().userId();
    }
}