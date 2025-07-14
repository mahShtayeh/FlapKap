package com.flapkap.vendingmachine.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.util.UUID;

/**
 * Exception representing a scenario where a user cannot be found in the system.
 * This exception is typically thrown when a requested user, identified by a UUID,
 * does not exist or cannot be located in the database or user repository.
 *
 * @author Mahmoud Shtayeh
 */
@Getter
@Builder
@RequiredArgsConstructor
public class UserNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -4947560651315491747L;

    /**
     * Unique identifier of the user that could not be found.
     * This identifier is represented as a universally unique identifier (UUID),
     * which ensures global uniqueness for the user in the system.
     */
    private final UUID userId;

    /**
     * Error message providing details about the exception.
     * This message is used to describe the reason or context
     * of the user not being found in the system, offering
     * additional clarity for debugging and user feedback.
     */
    private final String message;
}