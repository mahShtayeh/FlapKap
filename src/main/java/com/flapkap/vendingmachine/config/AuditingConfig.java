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
     * Provides an implementation of {@link AuditorAware} to retrieve the current auditor's
     * username for JPA auditing purposes. The auditor is determined based on the current
     * authentication context.
     *
     * @return an {@link AuditorAware} instance that supplies the current auditor's username
     * as an Optional.
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        final String username = switch(authentication) {
            case null -> SYSTEM_USERNAME;
            case Authentication auth when !auth.isAuthenticated() -> SYSTEM_USERNAME;
            case Authentication auth when auth.getPrincipal() instanceof UserPrincipal user -> user.getUsername();
            default -> ANONYMOUS_USERNAME;
        };

        return () -> Optional.of(username);
    }
}