package com.flapkap.vendingmachine.web.response;

import lombok.Builder;

@Builder
public record LoginResponse(
        String token
) {
}