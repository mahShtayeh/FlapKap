package com.flapkap.vendingmachine.security;

import com.flapkap.vendingmachine.exception.JwtAuthenticationException;
import com.flapkap.vendingmachine.service.impl.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * A utility class responsible for handling JSON Web Token (JWT) generation and validation.
 * This class is designed to work with Spring Security to facilitate token-based authentication and authorization.
 * It uses configurations from the JwtProperties class for setting up the secret key and token expiration duration.
 *
 * @author Mahmoud Shtayeh
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    /**
     * A constant representing the key used to store or retrieve the role claim within a JSON Web Token (JWT).
     * This key is used during the token generation and validation process to associate user roles
     * with the token and subsequently determine access rights based on those roles.
     */
    public static final String ROLES_CLAIM_KEY = "roles";

    /**
     * Holds the configuration properties for JSON Web Token (JWT) operations.
     * This variable is an instance of the {@code JwtProperties} class and provides
     * access to essential configuration values, such as the secret key used for
     * signing the token and the expiration duration for tokens.
     */
    private final JwtProperties jwtProperties;

    /**
     * A final field representing an instance of {@link UserDetailsServiceImpl}.
     * This service is responsible for handling user-authentication-related
     * operations by loading user-specific data, such as user details and roles,
     * during the authentication process.
     */
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Generates a JSON Web Token (JWT) for the provided user principal.
     * The token includes the user's unique identifier as the subject,
     * a claim containing the user's roles, an issue date, and an expiration date.
     *
     * @param userPrincipal the principal of the user, whose details are used to
     *                      generate the JWT. It must include the user's unique
     *                      identifier and roles (granted authorities).
     * @return a signed JWT in compact serialized format, ready for use in authentication mechanisms.
     */
    public String generateToken(final UserPrincipal userPrincipal) {
        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + jwtProperties.getExpirationMillis());

        return Jwts.builder()
                .subject(userPrincipal.getId().toString())
                .claim(ROLES_CLAIM_KEY, userPrincipal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Validates a JSON Web Token (JWT) to ensure it is properly signed using the configured signing key.
     * This method parses the token and verifies its signature to confirm its authenticity.
     *
     * @param token the JWT to be validated. It should be in a compact serialized format.
     * @return {@code true} if the token is successfully verified, indicating that it is valid and authentic.
     * This implementation always returns {@code true} upon successful validation.
     */
    public boolean validateToken(final String token) {
        Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);

        return true;
    }

    /**
     * Retrieves the signing key used for cryptographic operations like signing or verifying
     * JSON Web Tokens (JWTs). The signing key is derived from the secret key configured in
     * the {@link JwtProperties} and decoded using Base64.
     *
     * @return a SecretKey instance created from the decoded secret key, suitable for HMAC-based
     * cryptographic operations.
     */
    private SecretKey getSigningKey() {
        final byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Retrieves the authentication details by parsing and verifying the provided JWT token.
     * This method extracts the claims from the token, identifies the user's principal and roles,
     * and constructs an authentication object based on the user's details.
     *
     * @param token the JWT token to be parsed and verified. It must not be null and should be valid.
     * @return an {@link Authentication} object containing the user's principal and granted authorities.
     * @throws JwtAuthenticationException if the token is invalid, expired, or fails any verification checks.
     * @throws IllegalArgumentException   if the token is malformed or null.
     */
    public Authentication getAuthentication(final String token) {
        try {
            final Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            final UUID userId = UUID.fromString(claims.getSubject());
            final UserPrincipal userPrincipal = userDetailsService.loadUserById(userId);
            final Collection<? extends GrantedAuthority> authorities =
                    ((List<?>) claims.get(ROLES_CLAIM_KEY, List.class))
                            .stream()
                            .map(authority -> new SimpleGrantedAuthority((String) authority))
                            .toList();

            return new UsernamePasswordAuthenticationToken(
                    userPrincipal,
                    null,
                    authorities
            );
        } catch (JwtException | IllegalArgumentException ex) {
            throw JwtAuthenticationException.builder()
                    .message("error.jwt.authenticationFailed")
                    .build();
        }
    }
}