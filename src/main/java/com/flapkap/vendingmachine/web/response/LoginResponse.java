package com.flapkap.vendingmachine.web.response;

import lombok.Builder;
import lombok.NonNull;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;

/**
 * Represents the response returned after a successful login request.
 *
 * @param token The authentication token generated upon successful user login.
 *              This token is used for later authentication and authorization
 *              in the system. Ensure to keep it secure and confidential.
 * @author Mahmoud Shtayeh
 */
@Builder
public record LoginResponse(
        String token
) {
    /**
     * Returns a string representation of the LoginResponse object.
     * The token value is not included in plain text for security purposes.
     *
     * @return A formatted string representing the LoginResponse object,
     * indicating whether the token is present or empty.
     */
    @NonNull
    @Override
    public String toString() {
        return MessageFormat.format("LoginResponse[token={0}]",
                StringUtils.hasText(token) ? "[CONFIDENTIAL]" : "[EMPTY]");
    }
}