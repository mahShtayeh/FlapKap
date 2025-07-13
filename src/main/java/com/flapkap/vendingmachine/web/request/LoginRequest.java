package com.flapkap.vendingmachine.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Schema(description = "Login request")
@Builder
public record LoginRequest(
        @Schema(description = "User email", example = "test@flapkap.com")
        @NotEmpty(message = "error.user.missingOrEmptyEmail")
        @Email(message = "error.user.invalidEmail")
        String username,

        @Schema(description = "User password", example = "<PASSWORD>")
        @NotEmpty(message = "error.user.missingOrEmptyPassword")
        String password
) {
}