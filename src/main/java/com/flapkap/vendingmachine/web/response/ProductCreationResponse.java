package com.flapkap.vendingmachine.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

/**
 * Represents the response received upon successfully creating a product.
 *
 * @param productId A unique identifier for the created product.
 * @author Mahmoud Shtayeh
 */
@Schema(description = "Product creation response")
@Builder
public record ProductCreationResponse(
        @Schema(description = "Product unique ID")
        UUID productId
) {
}