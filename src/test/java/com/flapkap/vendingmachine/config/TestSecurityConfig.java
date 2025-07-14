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
 * TestSecurityConfig is a test configuration class that provides beans and configurations
 * for security-related components required during testing. It mocks key parts such
 * as the `JwtTokenProvider` and defines a custom `SecurityFilterChain` for testing purposes.
 *
 * @author Mahmoud Shtayeh
 */
@TestConfiguration
@NoArgsConstructor
public class TestSecurityConfig {
    /**
     * A constant UUID representing the unique identifier for a seller.
     * This value is used to define a static seller identity for testing purposes during
     * configurations and mock setups in the `TestSecurityConfig` class.
     */
    private static final UUID SELLER_ID = UUID.randomUUID();

    /**
     * A constant defining the role of a seller in the system.
     * It is used for setting authorities and permissions for users
     * identified as sellers during authentication and authorization processes.
     */
    private static final String SELLER_ROLE = "ROLE_SELLER";

    /**
     * Represents the static username of a seller used in the application's authentication and authorization process.
     * This constant is primarily used to populate mock data associated with the seller during various tests.
     * For instance, it is referenced as the username when building user credentials or principal in unit tests.
     */
    private static final String SELLER_USERNAME = "alen.martin@flapkap.com";

    /**
     * Represents an encrypted password used for authentication or security purposes within the application.
     * The value is a hashed string, typically generated using a robust encryption algorithm like BCrypt,
     * to ensure the confidentiality and integrity of sensitive information. This variable is immutable
     * and will not expose the raw password at any point within the codebase.
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
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/users",
                                "/api/v1/users/login"
                        ).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter(jwtTokenProvider()), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Creates a mock implementation of the {@link JwtTokenProvider} for testing purposes.
     * The mocked JwtTokenProvider is configured to always return valid results for token validation
     * and generate a fixed {@link Authentication} object for any input token.
     *
     * @return a mocked {@link JwtTokenProvider} instance with predefined behavior for testing.
     */
    @Bean
    @Primary
    public JwtTokenProvider jwtTokenProvider() {
        final JwtTokenProvider mockedJwtTokenProvider = mock(JwtTokenProvider.class);
        when(mockedJwtTokenProvider.validateToken(anyString())).thenReturn(true);
        when(mockedJwtTokenProvider.getAuthentication(anyString())).thenReturn(
                new UsernamePasswordAuthenticationToken(
                        UserPrincipal.builder()
                                .id(SELLER_ID)
                                .username(SELLER_USERNAME)
                                .password(ENCRYPTED_PASSWORD)
                                .authorities(Collections.singleton(new SimpleGrantedAuthority(SELLER_ROLE)))
                                .enabled(true)
                                .build(),
                        null,
                        Collections.singleton(new SimpleGrantedAuthority(SELLER_ROLE))
                )
        );

        return mockedJwtTokenProvider;
    }

    /**
     * Creates and returns an instance of {@link JwtAuthenticationFilter}, which is responsible
     * for processing authentication via JSON Web Tokens (JWT). The filter integrates with the
     * application's security filter chain to authenticate incoming requests.
     *
     * @param tokenProvider an instance of {@link JwtTokenProvider} used to validate and process JWT tokens
     * @return a new instance of {@link JwtAuthenticationFilter} configured with the provided {@link JwtTokenProvider}
     */
    @Bean
    @Primary
    public JwtAuthenticationFilter jwtAuthenticationFilter(final JwtTokenProvider tokenProvider) {
        return new JwtAuthenticationFilter(tokenProvider);
    }
}