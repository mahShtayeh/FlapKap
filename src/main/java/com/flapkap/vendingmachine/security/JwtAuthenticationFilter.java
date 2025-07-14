package com.flapkap.vendingmachine.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * A filter for processing authenticating requests using JSON Web Tokens (JWT).
 * This filter extends the {@code OncePerRequestFilter} to ensure that authentication is attempted
 * only once per request within Spring Security's filter chain.
 *
 * @author Mahmoud Shtayeh
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    /**
     * Represents the name of the HTTP header used for passing authorization credentials.
     * This header is commonly used to include access tokens or other credentials required
     * to authenticate and authorize requests securely.
     */
    public static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * Prefix used in the "Authorization" HTTP header to indicate that the token being used
     * follows the Bearer Token scheme. This value is essential for identifying and extracting
     * JSON Web Tokens (JWT) from the header during authentication processes.
     */
    public static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";

    /**
     * Defines the starting index within the "Authorization" HTTP header for extracting
     * the actual JSON Web Token (JWT) value after the "Bearer " prefix.
     */
    public static final int AUTHENTICATION_TOKEN_INDEX = 7;

    /**
     * A dependency that facilitates operations for generating, validating,
     * and processing JSON Web Tokens (JWT) within the authentication filter.
     * This instance of {@code JwtTokenProvider} is primarily used for extracting
     * user authentication information from tokens and validating token authenticity.
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Processes each HTTP request in the filter chain to authenticate the user by validating the provided JWT token
     * and setting the SecurityContext with the corresponding authentication details.
     * If the token is not valid or missing, the filter simply delegates the request through the filter chain.
     *
     * @param request     the incoming HTTP servlet request containing potential authentication information
     * @param response    the outgoing HTTP servlet response
     * @param filterChain the filter chain that this filter is part of, allowing the request to continue along the chain
     * @throws ServletException if the filtering process fails
     * @throws IOException      if there's an I/O error during request processing
     */
    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request,
                                    @NonNull final HttpServletResponse response,
                                    @NonNull final FilterChain filterChain) throws ServletException, IOException {
        final String jwtToken = getTokenFromRequest(request);

        if (StringUtils.hasText(jwtToken) && jwtTokenProvider.validateToken(jwtToken)) {
            final Authentication authentication = jwtTokenProvider.getAuthentication(jwtToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the Authorization header of the provided HTTP servlet request.
     * The method checks if the Authorization header contains a Bearer token and retrieves the
     * JWT token substring if present.
     *
     * @param request the HttpServletRequest containing the Authorization header, which may include
     *                the Bearer token.
     * @return the extracted JWT token as a String if the Authorization header contains a valid Bearer token;
     * otherwise, returns null.
     */
    private String getTokenFromRequest(final HttpServletRequest request) {
        final String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        String jwtToken = null;
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTHORIZATION_HEADER_PREFIX)) {
            jwtToken = bearerToken.substring(AUTHENTICATION_TOKEN_INDEX);
        }

        return jwtToken;
    }
}