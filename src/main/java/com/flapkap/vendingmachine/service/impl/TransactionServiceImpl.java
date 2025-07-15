package com.flapkap.vendingmachine.service.impl;

import com.flapkap.vendingmachine.dto.BuyDTO;
import com.flapkap.vendingmachine.exception.InsufficientFundsException;
import com.flapkap.vendingmachine.exception.UserNotFoundException;
import com.flapkap.vendingmachine.mapper.TransactionMapper;
import com.flapkap.vendingmachine.model.Product;
import com.flapkap.vendingmachine.model.User;
import com.flapkap.vendingmachine.service.ProductService;
import com.flapkap.vendingmachine.service.TransactionService;
import com.flapkap.vendingmachine.service.UserService;
import com.flapkap.vendingmachine.util.AssertUtil;
import com.flapkap.vendingmachine.util.CoinUtil;
import com.flapkap.vendingmachine.web.request.BuyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing deposit transactions in the vending machine system.
 * This interface defines the contract for interacting with buyer accounts by depositing coins
 * into their accounts. It ensures the update of the buyer's deposit balance based on the coins provided.
 *
 * @author Mahmoud Shtayeh
 */
@Transactional
@RequiredArgsConstructor
@Service("transactionService")
public class TransactionServiceImpl implements TransactionService {
    /**
     * A service used for managing user-related operations within the application.
     * This includes operations such as user registration, authentication, and retrieval.
     * It provides the ability to interact with user entities and execute business logic
     * associated with user management.
     */
    private final UserService userService;

    /**
     * A service used for managing product-related operations within the application.
     * This includes functionality for retrieving, updating, and interacting with products.
     * It serves as a centralized component for product management, enabling business logic
     * associated with product entities.
     */
    private final ProductService productService;

    /**
     * A mapper used for transforming transaction-related entities between different layers of the application.
     * This includes conversion between domain models and DTOs to facilitate interaction
     * and data transfer across various components of the service.
     */
    private final TransactionMapper transactionMapper;

    /**
     * Deposits a list of coins into a buyer's account and updates the buyer's deposit balance.
     *
     * @param buyerId the unique identifier of the buyer whose account will be credited
     * @param coins   the list of coins to be added to the buyer's deposit balance
     * @return the updated total deposit balance of the buyer
     * @throws UserNotFoundException if the buyer with the specified unique identifier is not found
     */
    @Override
    public Integer deposit(final UUID buyerId, final List<Integer> coins) {
        final User buyer = userService.read(buyerId);
        buyer.setDeposit(coins.stream().reduce(buyer.getDeposit(), Integer::sum));
        return buyer.getDeposit();
    }

    /**
     * Executes a buy operation for a list of products on behalf of a specified buyer.
     * This method updates the buyer's deposit balance, calculates the total amount spent,
     * and generates a summary of the purchased products and change.
     *
     * @param buyList the list of {@link BuyRequest} objects, each containing the product ID and amount to be purchased
     * @param buyerId the unique identifier of the buyer initiating the purchase
     * @return a {@link BuyDTO} instance containing details of the purchased products, total amount spent, and change
     * @throws UserNotFoundException      if the buyer with the specified unique identifier is not found
     * @throws InsufficientFundsException if the buyer's deposit is insufficient to complete the purchase
     */
    @Override
    public BuyDTO buy(final List<BuyRequest> buyList, final UUID buyerId) {
        final User buyer = userService.read(buyerId);

        final List<Product> boughtProducts = new ArrayList<>();
        final Integer totalSpent = buyList.stream()
                .map(buyRequest -> buyProduct(buyRequest, buyer, boughtProducts))
                .reduce(0, Integer::sum);

        return BuyDTO.builder()
                .boughtProducts(transactionMapper.toProductDTOs(boughtProducts))
                .totalSpent(totalSpent)
                .changes(CoinUtil.calculateChange(buyer.getDeposit()))
                .build();
    }

    /**
     * Resets the deposit balance of the specified buyer to zero.
     *
     * @param buyerId the unique identifier of the buyer whose deposit balance will be reset
     */
    @Override
    public void reset(final UUID buyerId) {
        final User buyer = userService.read(buyerId);
        buyer.setDeposit(0);
    }

    /**
     * Executes the purchase of a product for a specified buyer. This method processes the
     * transaction by checking sufficient funds, updating the buyer's deposit balance, reducing
     * the product stock, and appending the purchased product to the provided list.
     *
     * @param buyRequest     the buy request containing the product ID and quantity to be purchased
     * @param buyer          the buyer executing the purchase
     * @param boughtProducts the list to which the purchased product will be added
     * @return the total cost of the purchased product based on the quantity
     * @throws InsufficientFundsException if the buyer's deposit is insufficient to cover the total cost
     */
    private Integer buyProduct(final BuyRequest buyRequest, final User buyer, final List<Product> boughtProducts) {
        final Product product = productService.read(buyRequest.productId());
        final Integer price = product.getCost() * buyRequest.amount();

        AssertUtil.isTrue(buyer.getDeposit() > price,
                () -> new InsufficientFundsException("error.transaction.insufficientFunds"));

        buyer.setDeposit(buyer.getDeposit() - price);
        product.setAmount(product.getAmount() - buyRequest.amount());
        boughtProducts.add(product);
        return price;
    }
}