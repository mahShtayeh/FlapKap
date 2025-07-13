package com.flapkap.vendingmachine.security;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Represents the configuration properties for handling JSON Web Tokens (JWT) in the application.
 * This class is annotated with {@code @ConfigurationProperties} to enable external configuration
 * binding, allowing properties with the specified prefix ("jwt") to be mapped to this class's fields.
 *
 * @author Mahmoud Shtayeh
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    /**
     * Stores the secret key used for JWT generation and validation.
     * This key is critical for ensuring the integrity and authenticity of the tokens
     * by securely signing and verifying them during cryptographic operations.
     */
    private String secretKey;

    /**
     * Defines the duration in milliseconds for which a JSON Web Token (JWT) remains valid.
     * This value is used to calculate the token's expiration time by adding it to the current
     * timestamp during token generation. Once the token has expired, it can no longer be
     * authenticated or used for accessing secured resources.
     */
    private Long expirationMillis;
}