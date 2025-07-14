package com.flapkap.vendingmachine.service;

import com.flapkap.vendingmachine.model.Role;
import com.flapkap.vendingmachine.model.User;
import com.flapkap.vendingmachine.service.impl.TransactionServiceImpl;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link TransactionService} class.
 * This test class is responsible for verifying the behavior of the TransactionService
 * and ensuring its methods perform as expected in various scenarios.
 * It uses mocking to isolate dependencies and focus on the unit being tested.
 *
 * @author Mahmoud Shtayeh
 */
@NoArgsConstructor
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    /**
     * Mocked instance of {@link UserService} used for unit testing purposes.
     * This mock is used to simulate the behavior of the UserService without
     * requiring interaction with the actual implementation or underlying data sources.
     */
    @Mock
    private UserService userService;

    /**
     * A mocked instance of {@link TransactionServiceImpl} injected for unit testing purposes.
     * This variable is used in test cases to verify and validate the behavior of the
     * TransactionService implementation without relying on external dependencies.
     * It ensures the isolation of the tested unit and allows the use of mock behaviors
     * for dependencies.
     */
    @InjectMocks
    private TransactionServiceImpl transactionService;

    /**
     * A unique identifier representing the buyer entity used in test cases.
     * It is a constant UUID value generated randomly during initialization.
     * Primarily utilized for testing the behavior of methods in the TransactionServiceTest class.
     */
    private static final UUID BUYER_ID = UUID.randomUUID();

    /**
     * A list of coin denominations that are valid for deposits.
     * These values represent the allowed denominations in cents that can be deposited
     * into a user's account during transactions. The values are defined as follows:
     * 5, 10, 20, 50, and 100.
     */
    private static final List<Integer> DEPOSIT_COINS = List.of(5, 10, 20, 50, 100);

    /**
     * A constant representing the username of the buyer used in test cases.
     * This value is a static and final String that holds the predefined email address
     * associated with the buyer entity for testing purposes.
     * It is primarily used in scenarios where the buyer's username is needed
     * to ensure consistency and avoid redundancy in test setups.
     */
    private static final String BUYER_USERNAME = "jack.smith@flapkap.com";

    /**
     * Represents the initial balance value used for testing purposes
     * within the TransactionServiceTest class. This value is immutable
     * and constant throughout the test lifecycle.
     */
    private static final Integer BALANCE = 1000;

    /**
     * A static final variable that stores an encrypted password string.
     * The encryption is typically used to securely represent sensitive information.
     * This value simulates a hashed password and should not be directly exposed or manipulated.
     */
    private static final String ENCRYPTED_PASSWORD = "$2a$12$xJZv6T6OrSg4KUYAYOjISuL/9bh4ZhcEFYEIqMAzHKLXsf2MZL15G";

    /**
     * Represents a predefined instance of a {@link User} with the role of BUYER.
     * This constant is used for testing purposes, simulating a buyer with specific attributes.
     * This constant provides a standardized buyer user context for testing buyer-specific functionality
     * in the vending machine system.
     */
    private static final User BUYER = User.builder()
            .id(BUYER_ID)
            .username(BUYER_USERNAME)
            .password(ENCRYPTED_PASSWORD)
            .deposit(BALANCE)
            .role(Role.BUYER)
            .enabled(true)
            .build();

    /**
     * Tests the deposit functionality to ensure that a deposit of valid coins updates the buyer's balance correctly.
     * This test verifies:
     * - That the buyer's user information is retrieved using the provided buyer ID.
     * - That the deposit method accurately generates the updated balance by adding the deposit coins to the initial balance.
     * - That the balance calculated matches the expected total value.
     */
    @Test
    void deposit_withValidCoins_shouldReturnUpdatedBalance() {
        when(userService.read(BUYER_ID)).thenReturn(BUYER);

        final Integer balance = transactionService.deposit(BUYER_ID, DEPOSIT_COINS);

        assertThat(balance).isEqualTo(DEPOSIT_COINS.stream().reduce(BALANCE, Integer::sum));
    }
}
