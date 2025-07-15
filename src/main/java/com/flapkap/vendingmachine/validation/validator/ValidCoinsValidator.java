package com.flapkap.vendingmachine.validation.validator;

import com.flapkap.vendingmachine.util.CoinUtil;
import com.flapkap.vendingmachine.validation.annotation.ValidCoins;
import io.jsonwebtoken.lang.Collections;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * Implements the validation logic for the {@link ValidCoins} annotation.
 * This validator ensures that a list of coin values contains only valid
 * denominations defined in the allowed set.
 *
 * @author Mahmoud Shtayeh
 */
@NoArgsConstructor
public class ValidCoinsValidator implements ConstraintValidator<ValidCoins, List<Integer>> {
    /**
     * A set of allowed coin denominations used for validating coin values.
     * This set is initialized using the predefined coin denominations from {@link CoinUtil#COIN_DENOMINATIONS},
     * which represent valid coin values, such as 100, 50, 20, 10, and 5 cents.
     */
    private static final Set<Integer> ALLOWED_COINS = Collections.asSet(List.of(CoinUtil.COIN_DENOMINATIONS));

    /**
     * Validates whether the provided list of coin values meets the allowed denominations.
     * The list is considered valid if it is null, empty, or contains only values
     * from the predefined allowed set of coin denominations.
     *
     * @param coins   the list of coin values to validate
     * @param context the context in which the constraint is evaluated
     * @return true if the list is null, empty, or contains only allowed coin values,
     * false otherwise
     */
    @Override
    public boolean isValid(final List<Integer> coins, final ConstraintValidatorContext context) {
        return coins == null || coins.isEmpty() || ALLOWED_COINS.containsAll(coins);
    }
}