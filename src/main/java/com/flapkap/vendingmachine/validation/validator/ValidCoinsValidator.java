package com.flapkap.vendingmachine.validation.validator;

import com.flapkap.vendingmachine.validation.annotation.ValidCoins;
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
     * A constant set defining the allowed coin denominations for validation.
     * This set specifies that only coins with values of 5, 10, 20, 50, and 100
     * cents are considered valid. Used by the {@link ValidCoinsValidator} class
     * during validation to ensure that provided coin values match these allowed
     * denominations.
     */
    private static final Set<Integer> ALLOWED_COINS = Set.of(5, 10, 20, 50, 100);

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