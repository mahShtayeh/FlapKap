package com.flapkap.vendingmachine.dto;

import com.flapkap.vendingmachine.model.UserRole;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record UserDTO(
        String username,
        String password,
        BigDecimal deposit,
        UserRole role
) {
}