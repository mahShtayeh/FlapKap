package com.flapkap.vendingmachine.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;

/**
 * Exception representing an authentication error specific to JWT-based authentication mechanisms.
 *
 * @author Mahmoud Shtayeh
 */
@Getter
@Builder
@RequiredArgsConstructor
public class JwtAuthenticationException extends RuntimeException {
    /**
     * A unique identifier for the `JwtAuthenticationException` class, used during the serialization
     * and deserialization process to verify that the sender and receiver of a serialized object
     * maintain binary compatibility with the serialized class.
     */
    @Serial
    private static final long serialVersionUID = -5280351791019013033L;

    /**
     * The error message associated with the authentication exception.
     * This message provides a detailed description of the specific JWT authentication
     * failure encountered, aiding in debugging and providing user-friendly feedback.
     */
    private final String message;
}