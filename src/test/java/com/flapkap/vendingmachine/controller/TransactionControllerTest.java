package com.flapkap.vendingmachine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flapkap.vendingmachine.config.BuyerSecurityConfig;
import com.flapkap.vendingmachine.service.TransactionService;
import com.flapkap.vendingmachine.web.request.DepositRequest;
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
     * This list is immutable and defines the allowed coin values that can
     * be deposited by users in the application.
     */
    private static final List<Integer> DEPOSIT_COINS = List.of(5, 10, 20, 50, 100);

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
}
