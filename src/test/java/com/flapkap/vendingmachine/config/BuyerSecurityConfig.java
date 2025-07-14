package com.flapkap.vendingmachine.config;

import com.flapkap.vendingmachine.security.JwtAuthenticationFilter;
import com.flapkap.vendingmachine.security.JwtTokenProvider;
import com.flapkap.vendingmachine.security.UserPrincipal;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * A Spring Test Configuration class designed for security testing in the Buyer context.
 * This class provides mocked and configured beans required for security setup and authentication.
 *
 * @author Mahmoud Shtayeh
 */
@TestConfiguration
@NoArgsConstructor
public class BuyerSecurityConfig {
    /**
     * A constant UUID representing the unique identifier for a buyer.
     * This value is used to define a static buyer identity for testing purposes,
     * particularly in the context of security configurations and mock setups.
     */
    private static final UUID BUYER_ID = UUID.randomUUID();

    /**
     * A constant string representing the role of a buyer in the system.
     * This role is used to define authorities and permissions for users
     * identified as buyers during authentication and authorization processes.
     */
    private static final String BUYER_ROLE = "ROLE_BUYER";

    /**
     * Represents the static username of a buyer used in the application's authentication and authorization process.
     * This constant is primarily used for defining a fixed buyer identity during security configurations or
     * creating mock data for testing purposes. It serves as a unique identifier for a buyer in the system.
     */
    private static final String BUYER_USERNAME = "jack.smith@flapkap.com";

    /**
     * Represents an encrypted password used for authentication in the application.
     * The value is a secure, hashed string typically generated using an encryption algorithm
     * like BCrypt. This ensures that the actual password remains confidential and is not
     * directly exposed or stored in plaintext within the system.
     */
    private static final String ENCRYPTED_PASSWORD = "$2a$12$xJZv6T6OrSg4KUYAYOjISuL/9bh4ZhcEFYEIqMAzHKLXsf2MZL15G";

    /**
     * Configures and builds a {@link SecurityFilterChain} that defines security rules for handling HTTP requests.
     * This method disables CORS and CSRF, configures a stateless session management policy, specifies request matchers
     * for public endpoints, and ensures authentication for all other requests. Additionally, a JWT-based authentication
     * filter is added to the filter chain.
     *
     * @param http the {@link HttpSecurity} instance to modify and configure the security settings.
     * @return a fully configured {@link SecurityFilterChain} instance for managing application security.
     */
    @Bean
    @Primary
    @SneakyThrows
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/users",
                                "/api/v1/users/login"
                        ).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter(jwtTokenProvider()), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Provides a mocked instance of the {@link JwtTokenProvider} for testing or dependency injection purposes.
     * The mocked instance is configured to always return true for token validation and provides a predefined
     * {@link Authentication} object when retrieving authentication details from a token.
     *
     * @return a mocked instance of {@link JwtTokenProvider} with predefined behavior for testing.
     */
    @Bean
    @Primary
    public JwtTokenProvider jwtTokenProvider() {
        final JwtTokenProvider mockedJwtTokenProvider = mock(JwtTokenProvider.class);
        when(mockedJwtTokenProvider.validateToken(anyString())).thenReturn(true);
        when(mockedJwtTokenProvider.getAuthentication(anyString())).thenReturn(
                new UsernamePasswordAuthenticationToken(
                        UserPrincipal.builder()
                                .id(BUYER_ID)
                                .username(BUYER_USERNAME)
                                .password(ENCRYPTED_PASSWORD)
                                .authorities(Collections.singleton(new SimpleGrantedAuthority(BUYER_ROLE)))
                                .enabled(true)
                                .build(),
                        null,
                        Collections.singleton(new SimpleGrantedAuthority(BUYER_ROLE))
                )
        );

        return mockedJwtTokenProvider;
    }

    /**
     * Creates and registers a bean for the {@link JwtAuthenticationFilter}, which
     * is responsible for processing authenticating requests using JSON Web Tokens (JWT).
     *
     * @param tokenProvider an instance of {@link JwtTokenProvider} used to validate
     *                      and extract authentication details from JWT tokens.
     * @return an instance of {@link JwtAuthenticationFilter} initialized with the
     * injected {@link JwtTokenProvider}.
     */
    @Bean
    @Primary
    public JwtAuthenticationFilter jwtAuthenticationFilter(final JwtTokenProvider tokenProvider) {
        return new JwtAuthenticationFilter(tokenProvider);
    }
}