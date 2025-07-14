package com.flapkap.vendingmachine.service.impl;

import com.flapkap.vendingmachine.exception.UserNotFoundException;
import com.flapkap.vendingmachine.model.User;
import com.flapkap.vendingmachine.service.TransactionService;
import com.flapkap.vendingmachine.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}