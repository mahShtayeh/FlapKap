package com.flapkap.vendingmachine.service.impl;

import com.flapkap.vendingmachine.mapper.UserMapper;
import com.flapkap.vendingmachine.model.User;
import com.flapkap.vendingmachine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation of {@link UserDetailsService} responsible for managing user authentication details.
 * This class integrates with Spring Security to load user-specific data during the authentication process.
 * It relies on repository access, mapping utilities, and password encoding to provide secure and accurate
 * user details as required by the authentication framework.
 *
 * @author Mahmoud Shtayeh
 */
@Transactional
@RequiredArgsConstructor
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    /**
     * A repository interface for managing {@link User} entities.
     */
    private final UserRepository userRepository;

    /**
     * A component responsible for encoding raw passwords into a secure format suitable
     * for storage and password verification. This is typically used to hash passwords
     * before persisting them to ensure authentication security.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * A mapper interface used for mapping between domain objects and Data Transfer Objects (DTOs)
     * related to {@link User} entities. This interface simplifies and standardizes the conversion
     * processes, allowing the application to separate concerns between the domain layer and data transfer layer.
     */
    private final UserMapper userMapper;

    /**
     * Loads a user's details by their username.
     * This method retrieves a user entity from the database by username, maps it to the application's
     * {@link UserDetails} representation, and verifies the existence of the user. If no user is found, a
     * {@link UsernameNotFoundException} is thrown.
     *
     * @param username the username of the user whose details need to be loaded. It must not be null or empty.
     * @return a {@link UserDetails} object containing the user's information, including roles and authorities.
     * @throws UsernameNotFoundException if no user entity is found with the provided username.
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) {
        final User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("error.user.notFound"));

        return userMapper.toPrincipal(user);
    }
}