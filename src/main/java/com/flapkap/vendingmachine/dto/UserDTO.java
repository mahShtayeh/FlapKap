package com.flapkap.vendingmachine.dto;

import com.flapkap.vendingmachine.model.Role;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record UserDTO(
        String username,
        String password,
        BigDecimal deposit,
        Role role,
        boolean enabled
) {
}