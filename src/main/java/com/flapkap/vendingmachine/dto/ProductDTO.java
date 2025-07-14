package com.flapkap.vendingmachine.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ProductDTO(
        String name,
        double cost,
        Integer amount,
        String description,
        UUID sellerId
) {
}