package com.flapkap.vendingmachine.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Represents a User entity in the vending machine model.
 * This entity is mapped to the "USER" table in the database to avoid conflicts
 * with SQL reserved keywords. It contains fundamental attributes and
 * information intended for user identification and authentication.
 *
 * @author Mahmoud Shtayeh
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "\"USER\"") // user is preserved keyword in PostgreSQL
public class User extends Auditable {
    /**
     * A unique identifier for the User entity.
     * This identifier is automatically generated using the UUID strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Represents the username address associated with the user.
     * This field is used for user identification and communication purposes.
     */
    @NotEmpty(message = "error.user.missingOrEmptyEmail")
    @Email(message = "error.user.invalidEmail")
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Represents the password associated with the user.
     * This field is intended for user authentication and must be stored securely
     * to ensure confidentiality and prevent unauthorized access.
     */
    @NotEmpty(message = "error.user.missingOrEmptyPassword")
    @Column(nullable = false)
    private String password;

    /**
     * Represents the amount of money deposited by the user.
     * This field is used for tracking the balance that a user has available
     * for purchases or transactions within the vending machine system.
     */
    @NotNull(message = "error.user.missingDeposit")
    @PositiveOrZero(message = "error.user.invalidDeposit")
    private BigDecimal deposit;

    /**
     * Indicates whether the user account is enabled or active.
     * A `true` value signifies that the account is active and can interact with the system,
     * while a `false` value indicates that the account is disabled or inactive.
     */
    private boolean enabled;

    /**
     * Represents the role of the user within the vending machine system.
     * The role determines the user's level of access and the actions they are
     * authorized to perform. Supported values are defined in the {@link Role} enum,
     * which include roles such as BUYER and SELLER.
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
}