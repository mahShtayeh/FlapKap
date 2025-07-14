package com.flapkap.vendingmachine.controller;

import com.flapkap.vendingmachine.model.Role;
import com.flapkap.vendingmachine.web.RestResponse;
import com.flapkap.vendingmachine.web.request.LoginRequest;
import com.flapkap.vendingmachine.web.request.RegistrationRequest;
import com.flapkap.vendingmachine.web.response.LoginResponse;
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
     * The TestRestTemplate instance used for executing test HTTP requests to the application.
     * This field is automatically injected by Spring's dependency injection mechanism
     * and is primarily used in testing controllers to simulate client HTTP requests
     * and validate responses.
     */
    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * A format string used to construct URLs for test cases in the {@code AuthControllerTest} class.
     * Example structure: {@code "protocol:port/path"}.
     */
    private static final String URL_PATTERN = "%s:%d%s";

    /**
     * A constant representing the base URL for the test server during integration tests.
     */
    private static final String BASE_URL = "http://localhost";

    /**
     * Represents the API endpoint URL for user registration.
     */
    private static final String REGISTER_URL = "/api/v1/users";

    /**
     * Endpoint URL used for user login in the authentication process.
     */
    private static final String LOGIN_URL = "/api/v1/users/login";

    /**
     * A constant representing a predefined email address primarily used for testing
     * purposes in the integration tests of the {@code AuthControllerTest} class.
     * This value ensures consistency and avoids repetition in test cases where
     * a valid username is required.
     */
    private static final String BUYER_USERNAME = "jack.smith@flapkap.com";

    /**
     * Represents a predefined username used to simulate or test the functionality
     * of a seller entity within the scope of the UserServiceTest. This value serves
     * as a static reference for scenarios where an authenticated seller's username
     * is required, ensuring consistency across test cases.
     */
    private static final String SELLER_USERNAME = "alen.martin@flapkap.com";

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
    private static UUID buyerId;

    /**
     * Represents the JSON Web Token (JWT) generated upon the successful login of a buyer user.
     * This token is used for authenticating and authorizing subsequent API requests made by the
     * buyer during their session.
     */
    private static String buyerToken;

    /**
     * Represents the unique identifier for the seller used in the test cases.
     * This field is initialized and used to validate authentication
     * and registration scenarios related to the seller's operations.
     */
    private static UUID sellerId;

    /**
     * Holds the authentication token generated for the seller user after successful login.
     * This token is used to authorize subsequent requests made by the seller.
     */
    private static String sellerToken;

    /**
     * Tests the registration of a new seller user via the authentication controller.
     * The test creates a seller registration request with valid attributes and verifies
     * the appropriate creation of the seller in the application.
     * On successful registration, the unique seller ID is stored for further use.
     */
    @Test
    @Order(1)
    void registerSeller_withValidRequest_shouldReturn201() {
        final RegistrationRequest sellerCreationRequest = RegistrationRequest.builder()
                .username(SELLER_USERNAME)
                .password(HASHED_PASSWORD)
                .deposit(BALANCE)
                .role(Role.SELLER)
                .build();

        final ResponseEntity<RestResponse<RegistrationResponse>> response = restTemplate.exchange(
                String.format(URL_PATTERN, BASE_URL, port, REGISTER_URL),
                HttpMethod.POST,
                new HttpEntity<>(sellerCreationRequest),
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

        sellerId = responseBody.getPayload().userId();
    }

    /**
     * Tests the registration of a new buyer user via the authentication controller.
     * This test validates the behavior when a valid registration request is sent
     * to the registration endpoint. It ensures the HTTP 201 (Created) status
     * is returned and the response contains a valid payload including a unique user ID.
     */
    @Test
    @Order(2)
    void registerBuyer_withValidRequest_shouldReturn201() {
        final RegistrationRequest buyerCreationRequest = RegistrationRequest.builder()
                .username(BUYER_USERNAME)
                .password(HASHED_PASSWORD)
                .deposit(BALANCE)
                .role(Role.BUYER)
                .build();

        final ResponseEntity<RestResponse<RegistrationResponse>> response = restTemplate.exchange(
                String.format(URL_PATTERN, BASE_URL, port, REGISTER_URL),
                HttpMethod.POST,
                new HttpEntity<>(buyerCreationRequest),
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

        buyerId = responseBody.getPayload().userId();
    }

    /**
     * Validates the login functionality for a buyer user with a valid login request.
     * The purpose of this test is to ensure the login endpoint correctly processes valid
     * login requests and issues a JWT token for authenticated buyer users.
     */
    @Test
    @Order(3)
    void loginBuyer_withValidRequest_shouldReturnJwtToken() {
        final LoginRequest buyerLoginRequest = LoginRequest.builder()
                .username(BUYER_USERNAME)
                .password(HASHED_PASSWORD)
                .build();

        final ResponseEntity<RestResponse<LoginResponse>> response = restTemplate.exchange(
                String.format(URL_PATTERN, BASE_URL, port, LOGIN_URL),
                HttpMethod.POST,
                new HttpEntity<>(buyerLoginRequest),
                new ParameterizedTypeReference<>() {
                });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        final RestResponse<LoginResponse> responseBody = response.getBody();
        assertThat(responseBody)
                .isNotNull()
                .satisfies(body -> {
                    assertThat(body.getPayload()).isNotNull();
                    assertThat(body.getPayload().token()).isNotNull();
                });

        buyerToken = responseBody.getPayload().token();
    }

    /**
     * Tests the login functionality for a seller user with a valid login request.
     * Upon successful authentication, the JWT token for the seller is extracted
     * and stored for later operations in other test cases.
     */
    @Test
    @Order(4)
    void loginSeller_withValidRequest_shouldReturnJwtToken() {
        final LoginRequest sellerLoginRequest = LoginRequest.builder()
                .username(SELLER_USERNAME)
                .password(HASHED_PASSWORD)
                .build();

        final ResponseEntity<RestResponse<LoginResponse>> response = restTemplate.exchange(
                String.format(URL_PATTERN, BASE_URL, port, LOGIN_URL),
                HttpMethod.POST,
                new HttpEntity<>(sellerLoginRequest),
                new ParameterizedTypeReference<>() {
                });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        final RestResponse<LoginResponse> responseBody = response.getBody();
        assertThat(responseBody)
                .isNotNull()
                .satisfies(body -> {
                    assertThat(body.getPayload()).isNotNull();
                    assertThat(body.getPayload().token()).isNotNull();
                });

        sellerToken = responseBody.getPayload().token();
    }
}