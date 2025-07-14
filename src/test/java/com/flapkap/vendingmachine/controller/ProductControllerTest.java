package com.flapkap.vendingmachine.controller;

import com.flapkap.vendingmachine.config.TestSecurityConfig;
import com.flapkap.vendingmachine.mapper.ProductMapper;
import com.flapkap.vendingmachine.service.ProductService;
import com.flapkap.vendingmachine.service.UserService;
import com.flapkap.vendingmachine.web.request.ProductCreationRequest;
import com.flapkap.vendingmachine.web.response.ProductResponse;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.*;
import com.fasterxml.jackson.databind.*;

import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit test class for {@link ProductController} that verifies its behavior and functionality.
 * Uses Spring's {@link WebMvcTest} annotation to initialize only the web layer of the context.
 * Includes mocked dependencies and predefined constants to simulate real-world scenarios for testing.
 *
 * @author Mahmoud Shtayeh
 */
@NoArgsConstructor
@WebMvcTest(ProductController.class)
@Import(TestSecurityConfig.class)
class ProductControllerTest {
    /**
     * MockMvc instance used for performing HTTP requests in test scenarios.
     * It facilitates testing of controllers by mimicking the behavior of the Spring MVC framework.
     * This allows the execution of requests directly against the controller layer without starting a full web server.
     * Primarily used in unit and integration tests for verifying the behavior of Spring controllers.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Mocked instance of {@link ProductService} used for testing purposes within
     * {@code ProductControllerTest}. This mock allows for the simulation of product-related
     * operations, such as creating new products, without invoking the actual service logic.
     */
    @MockitoBean
    private ProductService productService;

    /**
     * Mocked instance of {@link UserService} for testing purposes in the {@code ProductControllerTest} class.
     * This variable is annotated with {@code @MockitoBean} to enable mocking and dependency injection
     * of the {@link UserService} interface during unit tests.
     */
    @MockitoBean
    private UserService userService;

    /**
     * A mock representation of the {@code ProductMapper} interface for testing purposes.
     * It is used to simulate the behavior of the actual mapper during unit tests in
     * the {@code ProductControllerTest} class.
     */
    @MockitoBean
    private ProductMapper productMapper;

    /**
     * An instance of {@link ObjectMapper} used for JSON processing in testing scenarios.
     * This component is automatically injected by the Spring framework to handle serialization
     * and deserialization of Java objects to and from JSON.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * A unique identifier for the product used in testing scenarios.
     * This constant is initialized with a randomly generated UUID to ensure
     * uniqueness across different test runs.
     */
    private static final UUID PRODUCT_ID = UUID.randomUUID();

    /**
     * Represents the name of the product used in testing scenarios.
     * This constant is used to simulate a product with a predefined
     * name within test cases.
     */
    private static final String PRODUCT_NAME = "Laptop";

    /**
     * Represents the fixed quantity of the product used within the test cases
     * in the {@code ProductServiceTest} class. This constant value is used
     * to validate functionality related to product amounts during test execution.
     */
    private static final Integer PRODUCT_AMOUNT = 200;

    /**
     * Represents the cost of a product in the context of product-related tests.
     * This is a constant value used as a reference for testing scenarios.
     */
    private static final Double PRODUCT_COST = 1500.0;

    /**
     * Constant representing the product description for testing purposes.
     * Used to simulate a product with specific attributes during test execution.
     * The value encapsulates concise details about the product.
     */
    private static final String PRODUCT_DESCRIPTION = "Cori9, Ram 16GB";

    /**
     * Mock JSON Web Token (JWT) string used for authorization in testing scenarios.
     * This constant simulates a valid bearer token required for API requests in
     * test cases involving authorization headers.
     */
    private static final String JWT_MOCK = "Bearer mock.jwt.token";

    /**
     * A constant instance of {@link ProductResponse} representing a predefined product response.
     * This instance is initialized with statically defined values for product ID, name, amount, cost,
     * and description. It is commonly used in tests to simulate a product response without relying
     * on dynamic data or external systems.
     */
    private static final ProductResponse PRODUCT_RESPONSE = ProductResponse.builder()
            .id(PRODUCT_ID)
            .name(PRODUCT_NAME)
            .amount(PRODUCT_AMOUNT)
            .cost(PRODUCT_COST)
            .description(PRODUCT_DESCRIPTION)
            .build();

    /**
     * Tests the successful creation of a product using the POST /api/products endpoint.
     * Validates the proper processing of the request and the expected response
     * when all required input data is provided and authorization is applied.
     */
    @Test
    @SneakyThrows
    @DisplayName("POST /api/products - Success")
    void createProduct_withValidDetails_shouldReturn201() {
        final ProductCreationRequest request = ProductCreationRequest.builder()
                .name(PRODUCT_NAME)
                .amount(PRODUCT_AMOUNT)
                .cost(PRODUCT_COST)
                .description(PRODUCT_DESCRIPTION)
                .build();

        given(productService.create(any())).willReturn(PRODUCT_ID);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", JWT_MOCK)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.payload.productId").value(PRODUCT_ID.toString()),
                        jsonPath("$.errors").isEmpty());
    }

    /**
     * Tests the successful retrieval of all products using the GET /api/products endpoint.
     * Mocks the behavior of the productService.readAll() method to return a predefined
     * list of products. The test also verifies that proper request headers, including
     * authorization, are included in the request.
     */
    @Test
    @SneakyThrows
    @DisplayName("GET /api/products - Success")
    void readAllProducts_shouldReturn200() {
        given(productService.readAll()).willReturn(List.of(PRODUCT_RESPONSE));

        mockMvc.perform(get("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", JWT_MOCK))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.payload").isArray(),
                        jsonPath("$.payload.length()").value(1),
                        jsonPath("$.errors").isEmpty());
    }
}