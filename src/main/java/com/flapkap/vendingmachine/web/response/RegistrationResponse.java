package com.flapkap.vendingmachine.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

/**
 * Represents the response received upon successful user registration.
 *
 * @param userId A unique identifier for the registered user.
 *
 * @author Mahmoud Shtayeh
 */
@Schema(description = "Registration response")
@Builder
public record RegistrationResponse(
        UUID userId
) {
}