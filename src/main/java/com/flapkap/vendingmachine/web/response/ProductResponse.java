package com.flapkap.vendingmachine.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

/**
 * Represents the response containing product details.
 *
 * @param id          The unique identifier for the product.
 * @param name        The name of the product.
 * @param cost        The cost of the product.
 * @param amount      The available quantity of the product.
 * @param description A textual description of the product.
 * @author Mahmoud Shtayeh
 */
@Schema(description = "Product response")
@Builder
public record ProductResponse(
        @Schema(description = "Product unique ID")
        UUID id,

        @Schema(description = "Product name", example = "Laptop")
        String name,

        @Schema(description = "Product cost", example = "1500.0")
        Double cost,

        @Schema(description = "Product amount", example = "200")
        Integer amount,

        @Schema(description = "Product description", example = "Cori9, RAM 16mg")
        String description
) {
}