package com.flapkap.vendingmachine.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Implementation of the {@link AuthenticationEntryPoint} interface used to handle unauthorized requests in
 * applications secured with Spring Security and JSON Web Token (JWT) authentication.
 *
 * @author Mahmoud Shtayeh
 */
@Component
@NoArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * A constant string representing the message "Unauthorized" used for responding
     * to unauthorized access attempts in the application. This message is typically
     * included in HTTP 401 Unauthorized responses to indicate that the requested
     * action or resource requires authentication or authorization that has either
     * failed or is missing.
     */
    public static final String UNAUTHORIZED_MESSAGE = "Unauthorized";

    /**
     * Handles unauthorized access to protected resources by responding with an
     * HTTP 401 Unauthorized status and a custom error message.
     *
     * @param request       the HttpServletRequest object that triggered the unauthorized access.
     * @param response      the HttpServletResponse object used to send the error response back to the client.
     * @param authException the exception thrown to indicate that authentication is required or has failed.
     * @throws IOException if an input or output error occurs while sending the error response.
     */
    @Override
    public void commence(final HttpServletRequest request,
                         final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, UNAUTHORIZED_MESSAGE);
    }
}