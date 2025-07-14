package com.flapkap.vendingmachine.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.util.UUID;

/**
 * Exception representing a scenario where a product cannot be found in the system.
 * This exception is typically thrown when a requested product, identified by a UUID,
 * is not available or cannot be located in the inventory or product repository.
 *
 * @author Mahmoud Shtayeh
 */
@Getter
@Builder
@RequiredArgsConstructor
public class ProductNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 3811774169483829587L;

    /**
     * Unique identifier of the product that could not be found.
     * This identifier is represented as a universally unique identifier (UUID),
     * which ensures global uniqueness for the product in the system.
     */
    private final UUID productId;

    /**
     * Error message providing additional details about the exception.
     * This message describes the context or reason why the product
     * could not be found in the system, helping to provide clarity
     * for debugging and error handling purposes.
     */
    private final String message;
}