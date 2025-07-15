package com.flapkap.vendingmachine.service;

import com.flapkap.vendingmachine.dto.BuyDTO;
import com.flapkap.vendingmachine.dto.ProductDTO;
import com.flapkap.vendingmachine.model.Role;
import com.flapkap.vendingmachine.model.User;
import com.flapkap.vendingmachine.service.impl.TransactionServiceImpl;
import com.flapkap.vendingmachine.web.request.BuyRequest;
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
     * Mocked instance of {@link ProductService}, used for simulating interactions
     * with the product-related operations during the unit tests in {@code TransactionServiceTest}.
     * This mock is primarily used for verifying the correctness of logic that involves
     * fetching, creating, updating, or deleting product-related data without relying on the actual
     * implementation of the {@link ProductService}.
     */
    @Mock
    private ProductService productService;

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
     * Primarily used for testing the behavior of methods in the TransactionServiceTest class.
     */
    private static final UUID BUYER_ID = UUID.randomUUID();

    /**
     * A unique identifier representing the product being tested in the
     * TransactionServiceTest class. This constant is used to simulate and identify
     * specific product-related operations, such as retrieval and purchase, during
     * the unit tests. The UUID value is generated dynamically for each test run.
     */
    private static final UUID PRODUCT_ID = UUID.randomUUID();

    /**
     * Represents the name of the product used in tests within the {@code TransactionServiceTest} class.
     * It holds the predefined name "V7 Can" to simulate product-related operations.
     */
    private static final String PRODUCT_NAME = "V7 Can";

    /**
     * The constant PRODUCT_AMOUNT represents the predefined quantity of a product
     * to be used in testing scenarios within the TransactionServiceTest class.
     * This value is used to validate functionality such as calculating total
     * costs or managing product transactions, ensuring consistency and accuracy
     * during test execution.
     */
    private static final Integer PRODUCT_AMOUNT = 100;

    /**
     * Represents the cost of a single product in the context of transaction-related
     * testing within the {@code TransactionServiceTest} class.
     * This constant is used to define a fixed product cost for validating functionality
     * related to transactions, such as purchasing products and calculating total costs.
     * The value is intended to serve as a standard reference for test scenarios involving
     * product pricing.
     */
    private static final Integer PRODUCT_COST = 5;

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
    private static final Integer BALANCE = 100;

    /**
     * A static final variable that stores an encrypted password string.
     * The encryption is typically used to securely represent sensitive information.
     * This value simulates a hashed password and should not be directly exposed or manipulated.
     */
    private static final String ENCRYPTED_PASSWORD = "$2a$12$xJZv6T6OrSg4KUYAYOjISuL/9bh4ZhcEFYEIqMAzHKLXsf2MZL15G";

    /**
     * Specifies the quantity of a product to be purchased in the test cases within the
     * {@code TransactionServiceTest} class.
     * This constantly simulates the number of units of a product a buyer wants to purchase
     * during test execution. It is used for validating the behavior of transaction-related
     * methods, ensuring that purchasing logic handles the specified quantity correctly.
     */
    private static final Integer TO_BUY_AMOUNT = 10;

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
     * A static, pre-configured {@link BuyRequest} instance used in test cases within the {@code TransactionServiceTest} class.
     * This object represents a predefined request to purchase a specific quantity of a product,
     * constructed using the product's unique identifier and the requested amount to buy.
     * This variable is immutable and utilized to validate buy operations under test scenarios.
     */
    private static final BuyRequest BUY_REQUEST = BuyRequest.builder()
            .productId(PRODUCT_ID)
            .amount(TO_BUY_AMOUNT)
            .build();

    /**
     * A static, constant instance of {@link ProductDTO} used for testing purposes.
     * This instance is immutable and intended to be used across multiple test cases within the test class to ensure
     * consistency and reusability when interacting with {@code ProductDTO} objects.
     */
    private static final ProductDTO PRODUCT_DTO = ProductDTO.builder()
            .id(PRODUCT_ID)
            .name(PRODUCT_NAME)
            .amount(PRODUCT_AMOUNT)
            .cost(PRODUCT_COST)
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
        final Integer initialBalance = BUYER.getDeposit();
        when(userService.read(BUYER_ID)).thenReturn(BUYER);

        final Integer balance = transactionService.deposit(BUYER_ID, DEPOSIT_COINS);

        assertThat(balance).isEqualTo(DEPOSIT_COINS.stream().reduce(initialBalance, Integer::sum));
    }

    /**
     * Tests the buy functionality to ensure that a buyer can purchase products using valid coins and that the correct
     * details are returned in the resulting BuyDTO object.
     * This test verifies:
     * - That the buyer's information is correctly retrieved using the provided buyer ID.
     * - That the product details of the requested product are retrieved using the product ID.
     * - That the buy operation returns a {@link BuyDTO} object with the correct list of bought products, total amount
     * spent, and details of the change.
     * - That the size and content of the bought products list correspond to the requested purchase.
     * - That the total spent amount is calculated accurately based on the quantity and cost of the product.
     * - That the resulting change list is correctly generated.
     */
    @Test
    void buyProduct_withValidCoins_shouldReturnBuyDTO() {
        when(userService.read(BUYER_ID)).thenReturn(BUYER);
        when(productService.read(PRODUCT_ID)).thenReturn(PRODUCT_DTO);

        final BuyDTO buyDTO = transactionService.buy(List.of(BUY_REQUEST), BUYER_ID);

        assertThat(buyDTO.boughtProducts()).isNotNull();
        assertThat(buyDTO.boughtProducts().size()).isEqualTo(1);
        assertThat(buyDTO.boughtProducts().getFirst()).isEqualTo(PRODUCT_DTO);

        assertThat(buyDTO.totalSpent()).isEqualTo(TO_BUY_AMOUNT * PRODUCT_COST);
        assertThat(buyDTO.changes().getFirst()).isNotNull();
    }

    /**
     * Verifies that the reset operation correctly sets the buyer's deposit balance to zero when a valid buyer ID is provided.
     * This test ensures:
     * - The buyer's information is successfully retrieved using the provided buyer ID.
     * - The reset operation updates the buyer's deposit balance to zero.
     * - The resulting deposit value matches the expected value of zero.
     */
    @Test
    void reset_withValidLogin_shouldResetBalance() {
        when(userService.read(BUYER_ID)).thenReturn(BUYER);

        transactionService.reset(BUYER_ID);

        assertThat(BUYER.getDeposit()).isZero();
    }
}