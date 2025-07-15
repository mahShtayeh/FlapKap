package com.flapkap.vendingmachine.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

/**
 * Represents a request object for updating a product.
 * This class encapsulates the necessary information for updating
 * the details of an existing product, including the name, cost, amount,
 * and an optional description. It is compatible with OpenAPI for API
 * documentation and includes validation annotations for input constraints.
 *
 * @author Mahmoud Shtayeh
 */
@Schema(description = "Product update request")
@Builder
public record ProductUpdateRequest(
        @Schema(description = "Product name", example = "Laptop")
        String name,

        @Schema(description = "Product cost", example = "1500.0")
        @PositiveOrZero(message = "error.product.invalidCost")
        Integer cost,

        @Schema(description = "Product amount", example = "200")
        @PositiveOrZero(message = "error.product.invalidAmount")
        Integer amount,

        @Schema(description = "Product description", example = "Cori9, RAM 16mg")
        String description
) {
}