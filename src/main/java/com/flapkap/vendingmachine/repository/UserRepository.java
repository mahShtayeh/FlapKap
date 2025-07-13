package com.flapkap.vendingmachine.repository;

import com.flapkap.vendingmachine.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link User} entities.
 * Extends the {@link JpaRepository} interface to provide CRUD operations,
 * pagination, and query support for the `Product` entity identified by a {@link UUID}.
 *
 * @author Mahmoud Shtayeh
 */
public interface UserRepository extends JpaRepository<User, UUID> {
    /**
     * Retrieves a user entity by its username.
     * This method is used to find a User entity based on the provided username.
     *
     * @param username the username of the user to be retrieved. It must not be null or empty.
     * @return an {@link Optional} containing the User entity if found, or an empty {@link Optional} if no user is associated with the given username.
     */
    Optional<User> findByUsername(String username);
}