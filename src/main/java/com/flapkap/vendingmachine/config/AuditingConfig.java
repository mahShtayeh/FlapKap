package com.flapkap.vendingmachine.config;

import com.flapkap.vendingmachine.security.UserPrincipal;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * JPA Auditing configurations
 *
 * @author Mahmoud Shtayeh
 */
@Configuration
@NoArgsConstructor
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditingConfig {
    /**
     * Represents the default system username used for auditing purposes when no authenticated
     * user is available. It is used to tag operations or actions in the system that
     * are performed automatically or without an authenticated user context.
     */
    private static final String SYSTEM_USERNAME = "FLAP_KAP";

    /**
     * Represents the default username used for auditing purposes when the user context exists,
     * but no valid authenticated user can be associated, such as anonymous actions or
     * unauthenticated requests within the application.
     */
    private static final String ANONYMOUS_USERNAME = "ANONYMOUS";

    /**
     * Provides an implementation of {@link AuditorAware} for determining the auditor's username
     * in JPA auditing. The method retrieves the username based on the current security context.
     * If no authenticated user is present, it returns a predefined system or anonymous username.
     *
     * @return an {@link AuditorAware} instance that resolves the current auditor's username
     * based on authentication state or predefined defaults.
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            String username = ANONYMOUS_USERNAME;
            if (authentication == null || !authentication.isAuthenticated()) {
                username = SYSTEM_USERNAME;
            } else if (authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
                username = userPrincipal.getUsername();
            }

            return Optional.of(username);
        };
    }
}