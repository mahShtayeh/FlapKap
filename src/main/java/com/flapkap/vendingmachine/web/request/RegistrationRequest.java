package com.flapkap.vendingmachine.web.request;

import com.flapkap.vendingmachine.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Schema(description = "Registration request")
@Builder
public record RegistrationRequest(
        @Schema(description = "User email", example = "test@flapkap.com")
        @NotEmpty(message = "error.user.missingOrEmptyEmail")
        @Email(message = "error.user.invalidEmail")
        String username,

        @Schema(description = "User password", example = "<PASSWORD>")
        @NotEmpty(message = "error.user.missingOrEmptyPassword")
        String password,

        @Schema(description = "User role", example = "BUYER")
        @NotNull(message = "error.user.missingRole")
        Role role,

        @Schema(description = "User enabled", example = "true")
        Boolean enabled
) {
}