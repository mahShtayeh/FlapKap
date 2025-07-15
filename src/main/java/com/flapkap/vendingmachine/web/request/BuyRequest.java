package com.flapkap.vendingmachine.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.util.UUID;

/**
 * Represents a request to purchase a product.
 * This request contains the essential details needed to execute a buy operation,
 * including the unique product identifier and the amount of the product to be purchased.
 * It validates the input to ensure that both fields are provided and the amount
 * is a positive value.
 *
 * @author Mahmoud Shtayeh
 */
@Schema(description = "Buy product request")
@Builder
public record BuyRequest(
        @Schema(description = "Product unique ID")
        @NotNull(message = "error.buy.missingProductId")
        UUID productId,

        @Schema(description = "Product amount")
        @NotNull(message = "error.buy.missingProductAmount")
        @Positive(message = "error.buy.invalidProductAmount")
        Integer amount
) {
}