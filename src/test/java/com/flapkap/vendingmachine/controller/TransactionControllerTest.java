package com.flapkap.vendingmachine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flapkap.vendingmachine.config.BuyerSecurityConfig;
import com.flapkap.vendingmachine.dto.BuyDTO;
import com.flapkap.vendingmachine.dto.ProductDTO;
import com.flapkap.vendingmachine.mapper.TransactionMapper;
import com.flapkap.vendingmachine.service.TransactionService;
import com.flapkap.vendingmachine.util.CoinUtil;
import com.flapkap.vendingmachine.web.request.BuyRequest;
import com.flapkap.vendingmachine.web.request.DepositRequest;
import com.flapkap.vendingmachine.web.response.BuyResponse;
import com.flapkap.vendingmachine.web.response.ProductResponse;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the TransactionController, which contains test cases to verify the functionality
 * of transaction-related endpoints provided by the controller. This includes testing deposit operations
 * and ensuring the expected behavior of the RESTful API under various conditions. The tests focus
 * on HTTP request handling and response validation at the controller layer using MockMvc.
 *
 * @author Mahmoud Shtayeh
 */
@NoArgsConstructor
@WebMvcTest(TransactionController.class)
@Import(BuyerSecurityConfig.class)
class TransactionControllerTest {
    /**
     * A MockMvc instance that is injected using Spring's @Autowired annotation.
     * It is used for testing the web layer of the application by performing
     * HTTP requests and verifying responses in the controller test class.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * An instance of the Jackson `ObjectMapper` configured and injected
     * by the Spring framework. This object is used for serializing and
     * deserializing JSON data in test cases within the
     * TransactionControllerTest class.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Mocked instance of the {@link TransactionService} used for testing purposes in the
     * {@code TransactionControllerTest} class. This service interface is responsible for
     * managing deposit transactions in the vending machine system, enabling interactions with
     * buyer accounts to update their deposit balances based on provided coins.
     */
    @MockitoBean
    private TransactionService transactionService;

    /**
     * Represents a mocked instance of the TransactionMapper interface used in the context of
     * unit tests within the test class TransactionControllerTest. This mock is used to test
     * and verify transaction-related functionality by simulating the behavior of the real TransactionMapper.
     * This mocked object is managed by the Mockito framework, which provides the ability to
     * define mock behavior and verify interactions for test scenarios.
     */
    @MockitoBean
    private TransactionMapper transactionMapper;

    /**
     * A constant representing the key for the Authorization header in HTTP requests.
     * Used to specify the authorization credentials in the request headers during
     * tests in the TransactionControllerTest class.
     */
    private static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * A mock JSON Web Token (JWT) used for testing purposes in the TransactionControllerTest class.
     * This token is utilized as a placeholder authorization token in HTTP request headers
     * to simulate authentication during test cases.
     */
    private static final String JWT_MOCK = "Bearer mock.jwt.token";

    /**
     * Represents the initial balance assigned to a user account for transaction-related tests.
     * This value is used as the default starting point for balance calculations and validations
     * during the execution of test cases in the TransactionControllerTest class.
     */
    private static final Integer INITIAL_BALANCE = 1000;

    /**
     * A list of coin denominations accepted for deposit transactions.
     * The values represent the supported coin units in cents.
     * This list is immutable and defines the allowed coin values that users can
     * deposit in the application.
     */
    private static final List<Integer> DEPOSIT_COINS = List.of(5, 10, 20, 50, 100);

    /**
     * A unique identifier for the product used in test cases. This UUID is
     * automatically generated during initialization to simulate a unique
     * product ID for verifying transactions within the test context.
     */
    private static final UUID PRODUCT_ID = UUID.randomUUID();

    /**
     * Represents the name of the product being utilized in the test scenarios within
     * the TransactionControllerTest class. This constant helps identify the specific
     * product being tested or manipulated in various transactions.
     */
    private static final String PRODUCT_NAME = "V7 Can";

    /**
     * Represents the default amount of a product available for testing within the
     * {@code TransactionControllerTest} class.
     * This constant is used for validating and simulating various transaction operations,
     * such as buying and managing products, where product availability plays a key role.
     */
    private static final Integer PRODUCT_AMOUNT = 100;

    /**
     * Represents the cost of a specific product used in the transaction operations.
     * This constant is primarily used in test cases to validate purchasing and
     * balance computations.
     */
    private static final Integer PRODUCT_COST = 5;

    /**
     * Represents the fixed number of items to be purchased during transaction tests.
     * This constant is used in test scenarios to simulate purchase operations
     * ensuring consistent test behavior across different methods in the
     * TransactionControllerTest class.
     */
    private static final Integer TO_BUY_AMOUNT = 10;

    /**
     * Represents a predefined buyer's balance used as a constant value for testing purposes
     * in the {@code TransactionControllerTest} class.
     * This variable is utilized in scenarios where the initial balance of a buyer needs
     * to be referenced, such as testing deposit and purchase functionalities.
     */
    private static final Integer BUYER_BALANCE = 100;

    /**
     * A pre-defined static instance of {@link ProductDTO} used for testing within the context of
     * {@code TransactionControllerTest}. This instance represents a mock product with specific attributes,
     * such as ID, name, cost, and amount, which can be used in various unit tests to validate
     * the behavior of transactional operations.
     * This object is constructed using the {@code ProductDTO.builder()} method, ensuring immutability
     * and consistency of the test data while providing a clear definition of its attributes.
     */
    private static final ProductDTO PRODUCT_DTO = ProductDTO.builder()
            .id(PRODUCT_ID)
            .name(PRODUCT_NAME)
            .amount(PRODUCT_AMOUNT)
            .cost(PRODUCT_COST)
            .build();

    /**
     * A pre-defined static instance of {@link ProductResponse} used for testing purposes in the
     * {@code TransactionControllerTest}.
     * The {@code PRODUCT_RESPONSE} variable encapsulates a product's key properties including its
     * unique identifier, name, available quantity, and cost. This instance is statically constructed
     * with preset mock values using {@link ProductResponse#builder()} for test scenarios.
     */
    private static final ProductResponse PRODUCT_RESPONSE = ProductResponse.builder()
            .id(PRODUCT_ID)
            .name(PRODUCT_NAME)
            .amount(PRODUCT_AMOUNT)
            .cost(PRODUCT_COST)
            .build();

    /**
     * Tests the deposit functionality of the transaction controller using a valid list of coins.
     * The test verifies that the POST request to the deposit endpoint correctly processes and
     * updates the user's current balance with the specified coins. The expected behaviors tested include:
     * - Returning an HTTP 200 OK status.
     * - Returning the correct updated balance in the response payload.
     * - Ensuring there are no errors in the response.
     */
    @Test
    @SneakyThrows
    @DisplayName("POST /api/v1/transactions/deposit - Success")
    void deposit_withValidCoins_shouldReturnCurrentBalance() {
        final DepositRequest request = DepositRequest.builder()
                .coins(DEPOSIT_COINS)
                .build();
        final Integer updatingBalance = DEPOSIT_COINS.stream().reduce(INITIAL_BALANCE, Integer::sum);

        given(transactionService.deposit(any(), eq(DEPOSIT_COINS))).willReturn(updatingBalance);

        mockMvc.perform(post("/api/v1/transactions/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, JWT_MOCK)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.payload.currentBalance").value(updatingBalance.toString()),
                        jsonPath("$.errors").isEmpty());
    }

    /**
     * Tests the buy operation of the transaction controller when provided with valid product details.
     * This test verifies the following behaviors:
     * - A POST request to the `/api/v1/transactions/buy` endpoint processes the input correctly.
     * - The operation returns an HTTP 200 OK status.
     * - The response payload includes a list of successfully purchased products.
     * - The total amount spent is accurately calculated and returned.
     * - The correct change is calculated and returned as part of the response.
     * The input contains a valid list of {@link BuyRequest}, each specifying the product ID and quantity.
     * Uses assertions to verify the content and structure of the returned response.
     */
    @Test
    @SneakyThrows
    @DisplayName("POST /api/v1/transactions/buy - Success")
    void buy_withValidDetails_shouldReturnBuyReport() {
        final List<BuyRequest> request = List.of(BuyRequest.builder()
                .productId(PRODUCT_ID)
                .amount(TO_BUY_AMOUNT)
                .build());

        given(transactionService.buy(any(), any())).willReturn(BuyDTO.builder()
                .boughtProducts(List.of(PRODUCT_DTO))
                .totalSpent(TO_BUY_AMOUNT * PRODUCT_COST)
                .changes(CoinUtil.calculateChange(BUYER_BALANCE - (TO_BUY_AMOUNT * PRODUCT_COST)))
                .build());

        given(transactionMapper.toResponse(any())).willReturn(BuyResponse.builder()
                .boughtProducts(List.of(PRODUCT_RESPONSE))
                .totalSpent(TO_BUY_AMOUNT * PRODUCT_COST)
                .changes(CoinUtil.calculateChange(BUYER_BALANCE - (TO_BUY_AMOUNT * PRODUCT_COST)))
                .build());

        mockMvc.perform(post("/api/v1/transactions/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, JWT_MOCK)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.payload.boughtProducts").isArray(),
                        jsonPath("$.payload.boughtProducts").isNotEmpty(),
                        jsonPath("$.payload.totalSpent").value(String.valueOf(TO_BUY_AMOUNT * PRODUCT_COST)),
                        jsonPath("$.payload.changes").isArray(),
                        jsonPath("$.payload.changes").isNotEmpty());
    }
}