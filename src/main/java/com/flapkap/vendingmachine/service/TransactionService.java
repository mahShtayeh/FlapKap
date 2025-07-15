package com.flapkap.vendingmachine.service;

import com.flapkap.vendingmachine.dto.BuyDTO;
import com.flapkap.vendingmachine.exception.UserNotFoundException;
import com.flapkap.vendingmachine.web.request.BuyRequest;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing deposit transactions in the vending machine system.
 * This interface defines the contract for interacting with buyer accounts by depositing coins
 * into their accounts. It ensures the update of the buyer's deposit balance based on the coins provided.
 *
 * @author Mahmoud Shtayeh
 */
public interface TransactionService {
    /**
     * Deposits a list of coins into a buyer's account and updates the buyer's deposit balance.
     *
     * @param buyerId the unique identifier of the buyer whose account will be credited
     * @param coins   the list of coins to be added to the buyer's deposit balance
     * @return the updated total deposit balance of the buyer
     * @throws UserNotFoundException if the buyer with the specified unique identifier is not found
     */
    Integer deposit(UUID buyerId, List<Integer> coins);

    /**
     * Processes a buy operation for a list of requested products by a specific buyer.
     * Updates the buyer's account, calculates the total cost of the purchased products,
     * and determines the change to be returned based on the buyer's deposit balance.
     *
     * @param buyList a list of {@link BuyRequest} representing the products to be purchased along with their respective quantities
     * @param buyerId the unique identifier of the buyer executing the purchase
     * @return a {@link BuyDTO} containing details of the purchased products, total cost, and any change to be returned
     */
    BuyDTO buy(List<BuyRequest> buyList, UUID buyerId);

    /**
     * Resets the deposit balance of the specified buyer to zero.
     *
     * @param buyerId the unique identifier of the buyer whose deposit balance will be reset
     */
    void reset(UUID buyerId);
}