package com.flapkap.vendingmachine.dto;

import lombok.Builder;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) representing a product.
 * This class is used to encapsulate product data exchanged between different layers
 * of an application, such as service, controller, and client layers.
 * It serves as an immutable representation of a product's core attributes.
 * The {@code ProductDTO} uses a record structure for immutability and concise syntax.
 * Fields:
 * - {@code name}: The name of the product.
 * - {@code cost}: The monetary cost of the product, represented as a {@code double}.
 * - {@code amount}: The quantity of the product available, represented as an {@code Integer}.
 * - {@code description}: A textual description providing additional details about the product.
 * - {@code sellerId}: A unique identifier ({@link UUID}) representing the seller associated with the product.
 *
 * @author Mahmoud Shtayeh
 */
@Builder
public record ProductDTO(
        String name,
        Double cost,
        Integer amount,
        String description,
        UUID sellerId
) {
}