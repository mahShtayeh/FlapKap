package com.flapkap.vendingmachine.security;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.UUID;

/**
 * Represents the principal of a user in the application, implementing the UserDetails interface
 * from Spring Security to facilitate authentication and authorization processes.
 * This class encapsulates the core details of a user required for security operations,
 * such as a unique identifier, username, password, account status, and assigned roles or authorities.
 * It is commonly used to build authentication tokens and validate user-based access control.
 *
 * @author Mahmoud Shtayeh
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserPrincipal implements UserDetails {
    /**
     * A constant used to define the serial version unique identifier for the {@code UserPrincipal} class.
     * This identifier is used during the deserialization process to verify that the sender and receiver
     * of a serialized object maintain compatibility with respect to the object's class.
     */
    @Serial
    private static final long serialVersionUID = -2488074438069343020L;

    /**
     * A unique identifier for the user, represented as a UUID.
     * This ID is used as the subject claim in generated JWT tokens
     * to provide a guaranteed unique reference for the user within the system.
     */
    private UUID id;

    /**
     * Represents the username of the user.
     * This field is used as the primary identifier during authentication processes
     * and is essential for user identity verification within the application.
     * It must be unique for each user and is commonly paired with a password
     * or other credentials to establish secure access.
     */
    private String username;

    /**
     * Stores the password of the user.
     * This field is a critical part of the authentication process and is used to verify the user's identity.
     * It should be securely hashed and stored to ensure the integrity and confidentiality of user credentials.
     * Typically paired with the username for secure login operations.
     */
    private String password;

    /**
     * Indicates whether the user account is enabled.
     * This field is used to define the active status of a user account in the system.
     * If set to {@code true}, the user account is enabled and can authenticate successfully.
     * If set to {@code false}, the user account is disabled and will be rejected during authentication.
     */
    private boolean enabled;

    /**
     * Represents the collection of granted authorities assigned to the user.
     * Authorities define the roles or permissions that determine the user's access level
     * within the application. These are typically used by the Spring Security framework
     * to control access to resources based on the user's roles or granted permissions.
     */
    private Collection<? extends GrantedAuthority> authorities;
}