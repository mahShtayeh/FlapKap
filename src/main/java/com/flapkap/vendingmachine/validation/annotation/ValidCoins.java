package com.flapkap.vendingmachine.validation.annotation;

import com.flapkap.vendingmachine.validation.validator.ValidCoinsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidCoinsValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCoins {
    /**
     * Defines the default validation message for invalid coin values.
     * This message is returned when the provided coin values do not match
     * the allowed denominations (5, 10, 20, 50, or 100 cents).
     *
     * @return the default validation message for invalid coin values
     */
    String message() default "Invalid coins values, Coins must be 5, 10, 20, 50 or 100 Cent coins";

    /**
     * Specifies the validation groups for which this validation constraint applies.
     * Groups are used during the validation phase to conditionally apply constraints
     * based on the group or groups provided.
     *
     * @return an array of classes representing validation groups
     */
    Class<?>[] groups() default {};

    /**
     * Assigns custom payload objects to the constraint. Payloads can be used
     * by clients of the validation framework to associate additional metadata
     * or processing logic with a given constraint.
     *
     * @return an array of payload classes associated with the constraint
     */
    Class<? extends Payload>[] payload() default {};
}