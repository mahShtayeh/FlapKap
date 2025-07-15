package com.flapkap.vendingmachine.exception;

import java.io.Serial;

/**
 * Exception representing a scenario where a requested operation cannot be completed due to
 * insufficient funds. This custom exception is a subclass of {@link RuntimeException}.
 * The exception includes a message providing additional details about the cause of the error.
 *
 * @author Mahmoud Shtayeh
 */
public class InsufficientFundsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1064053640885166657L;

    /**
     * Constructs a new InsufficientFundsException with the specified detail message.
     * The message provides additional information about the reason for the exception.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public InsufficientFundsException(final String message) {
        super(message);
    }
}