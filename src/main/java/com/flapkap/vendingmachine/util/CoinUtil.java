package com.flapkap.vendingmachine.util;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for coin-related operations, primarily focused on
 * calculating change based on available coin denominations.
 *
 * @author Mahmoud Shtayeh
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CoinUtil {
    /**
     * An array of coin denominations, sorted in descending order,
     * used for calculating change in cents.
     * The values represent the denominations in cent units (e.g., 100 for $1, 50 for $0.50, etc.).
     */
    public static final Integer[] COIN_DENOMINATIONS = {100, 50, 20, 10, 5}; // Sorted descending

    /**
     * Calculates the change for a given amount in cents using
     * predefined coin denominations in descending order.
     *
     * @param amountInCents the total amount in cents for which change needs to be calculated
     * @return a list of CoinChange objects, where each object represents a denomination and its count
     */
    public static List<CoinChange> calculateChange(final Integer amountInCents) {
        int remaining = amountInCents;
        final List<CoinChange> changes = new ArrayList<>();
        for (final Integer coin : COIN_DENOMINATIONS) {
            if (remaining >= coin) {
                final int count = remaining / coin;
                changes.add(CoinChange.builder()
                        .coin(coin)
                        .count(count)
                        .build());
                remaining %= coin;
            }
        }

        return changes;
    }

    /**
     * Represents a single coin denomination and the number of coins of that denomination.
     * This class is used to store and manipulate the breakdown of coins required to make
     * a specific amount of change.
     *
     * @author Mahmoud Shtayeh
     */
    @Getter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CoinChange {
        /**
         * Represents the denomination of a coin in cents.
         * This field specifies the value of the coin associated with a particular instance of the CoinChange class.
         * Used primarily for representing and manipulating coin denominations during change calculations.
         * The value is immutable and set during the construction of the object.
         */
        private Integer coin;

        /**
         * Represents the count of coins for a specific denomination.
         * This field specifies how many coins of a certain denomination are used
         * in the breakdown of a total amount during change calculation.
         * The value can vary and is set dynamically during the computation process.
         */
        private Integer count;
    }
}