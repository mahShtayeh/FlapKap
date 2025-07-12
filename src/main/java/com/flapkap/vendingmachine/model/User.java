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
public class User {
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
    private String username;

    /**
     * Represents the password associated with the user.
     * This field is intended for user authentication and must be stored securely
     * to ensure confidentiality and prevent unauthorized access.
     */
    @NotEmpty(message = "error.user.missingOrEmptyPassword")
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
     * Represents the role assigned to the user.
     * This attribute defines whether the user acts as a `BUYER` or `SELLER`
     * within the vending machine system. The role determines the user's
     * permissions and available actions in the application.
     */
    @Enumerated(EnumType.STRING)
    @NotNull(message = "error.user.missingRole")
    private UserRole role;
}