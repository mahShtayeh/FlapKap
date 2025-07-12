package com.flapkap.vendingmachine.service;

import com.flapkap.vendingmachine.dto.UserDTO;
import com.flapkap.vendingmachine.model.User;

import java.util.UUID;

/**
 * Service interface for managing user-related operations.
 * This interface defines methods for application-specific business logic related to
 * {@link User} entities.
 * It acts as a contract to be implemented by service classes that interact with
 * data repositories and provide higher-level operations for user management.
 *
 * @author Mahmoud Shtayeh
 */
public interface UserService {
    /**
     * Registers a new user in the system by saving the provided user details and returning
     * the unique identifier of the created user entity.
     *
     * @param userDTO the Data Transfer Object containing the details of the user to be registered
     * @return the unique {@link UUID} identifier of the newly created user
     */
    UUID register(UserDTO userDTO);
}