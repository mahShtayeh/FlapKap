package com.flapkap.vendingmachine.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

/**
 * A request object used to encapsulate data required for creating a new product.
 * It contains details such as the product's name, cost, amount, and an optional description.
 *
 * @author Mahmoud Shtayeh
 */
@Schema(description = "Creat product request")
@Builder
public record ProductCreationRequest(
        @Schema(description = "Product name", example = "Laptop")
        @NotEmpty(message = "error.product.missingOrEmptyName")
        String name,

        @Schema(description = "Product cost", example = "15.0")
        @NotNull(message = "error.product.missingCost")
        @PositiveOrZero(message = "error.product.invalidCost")
        double cost,

        @Schema(description = "Product amount", example = "200")
        @NotNull(message = "error.product.missingAmount")
        @PositiveOrZero(message = "error.product.invalidAmount")
        Integer amount,

        @Schema(description = "Product description", example = "Cori9, RAM 16mg")
        String description
) {
}