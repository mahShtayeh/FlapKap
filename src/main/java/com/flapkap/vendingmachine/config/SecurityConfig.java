package com.flapkap.vendingmachine.config;

import com.flapkap.vendingmachine.security.JwtAuthenticationEntryPoint;
import com.flapkap.vendingmachine.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration class for the application, responsible for defining the
 * security settings, authentication mechanisms, and enabling token-based security
 * using JWT. Integrates JwtAuthenticationFilter for request filtering and
 * JwtAuthenticationEntryPoint for handling unauthorized access attempts.
 * This class also defines the necessary security-related beans for password encoding
 * and authentication management.
 *
 * @author Mahmoud Shtayeh
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    /**
     * Defines the strength parameter used with the BCrypt password hashing algorithm.
     * This parameter dictates the computational complexity and the resulting time
     * required to hash a password.
     * The default recommended value is 12, balancing security and performance.
     */
    public static final Integer BCRYPT_STRENGTH = 12;

    /**
     * A custom filter that handles the processing and validation of JSON Web Tokens (JWT)
     * for incoming HTTP requests. This filter intercepts requests in the security filter chain,
     * examines the Authorization header for a valid Bearer token, and populates the security context
     * if the token is successfully validated.
     */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * An instance of {@link JwtAuthenticationEntryPoint} used to handle unauthorized access
     * attempts to secured resources. This component is responsible for returning an HTTP
     * 401 Unauthorized response when an authentication exception occurs during an
     * incoming request.
     */
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**
     * Configures a SecurityFilterChain bean responsible for establishing security rules
     * and filters within the application. This configuration includes disabling CORS and CSRF,
     * setting session management to stateless, handling unauthorized access, and defining
     * authorization rules for specific endpoints.
     * The method also integrates a custom JWT authentication filter to process and validate
     * JWT tokens for protected resources and adds it to the security filter chain.
     *
     * @param http the {@link HttpSecurity} object used to configure HTTP security settings,
     *             such as CORS, CSRF, session management, exception handling, and request
     *             authorization policies.
     * @return the fully configured {@link SecurityFilterChain} instance.
     */
    @Bean
    @SneakyThrows
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/users",
                                "/api/v1/users/logins"
                        ).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Creates and configures a {@link PasswordEncoder} bean using BCrypt hashing algorithm
     * with specified strength.
     * The strength parameter defines the computational complexity of the encoding
     * process (default recommendation is 12).
     *
     * @return a {@link PasswordEncoder} instance configured with BCrypt hashing.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(BCRYPT_STRENGTH); // Strength = 12 (recommended)
    }

    /**
     * Configures and provides an {@link AuthenticationManager} bean, which is responsible for
     * authentication operations and integrates with the security framework to manage authentication processes.
     *
     * @param authConfiguration the {@link AuthenticationConfiguration} instance used to retrieve the
     *                          authentication manager.
     * @return the configured {@link AuthenticationManager} instance.
     */
    @Bean
    @SneakyThrows
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authConfiguration) {
        return authConfiguration.getAuthenticationManager();
    }
}