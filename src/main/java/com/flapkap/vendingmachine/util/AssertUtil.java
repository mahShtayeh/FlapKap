package com.flapkap.vendingmachine.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

/**
 * Util to enable firing custom exception when fails
 *
 * @author Mahmoud Shtayeh
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AssertUtil {
    /**
     * Fire custom exception when the expression is false
     *
     * @param expression        based on the exception is being fired
     * @param exceptionSupplier provide the exception when needed
     */
    public static void isTrue(final boolean expression, final Supplier<? extends RuntimeException> exceptionSupplier) {
        if (!expression) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Ensures that the provided object is not null, throwing a custom exception
     * if the object is null.
     *
     * @param <T>               the type of the object to check
     * @param target            the object to validate as not null
     * @param exceptionSupplier a supplier that provides the exception to be thrown
     *                          if the object is null
     * @throws RuntimeException if the provided object is null
     */
    public static <T> void notNull(final T target, final Supplier<? extends RuntimeException> exceptionSupplier) {
        if (target == null) {
            throw exceptionSupplier.get();
        }
    }
}