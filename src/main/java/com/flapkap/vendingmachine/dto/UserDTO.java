package com.flapkap.vendingmachine.dto;

import com.flapkap.vendingmachine.model.Role;
import lombok.Builder;

@Builder
public record UserDTO(
        String username,
        String password,
        Integer deposit,
        Role role,
        boolean enabled
) {
}