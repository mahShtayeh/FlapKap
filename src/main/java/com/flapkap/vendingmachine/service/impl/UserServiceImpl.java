package com.flapkap.vendingmachine.service.impl;

import com.flapkap.vendingmachine.dto.UserDTO;
import com.flapkap.vendingmachine.mapper.UserMapper;
import com.flapkap.vendingmachine.repository.UserRepository;
import com.flapkap.vendingmachine.security.JwtTokenProvider;
import com.flapkap.vendingmachine.security.UserPrincipal;
import com.flapkap.vendingmachine.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.flapkap.vendingmachine.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of the {@link UserService} interface responsible for handling
 * business logic related to {@link User} entities.
 *
 * @author Mahmoud Shtayeh
 */
@Transactional
@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    /**
     * The {@code authenticationManager} is a core Spring Security component responsible for
     * processing authentication requests. It manages the authentication process by delegating to a
     * collection of {@link org.springframework.security.authentication.AuthenticationProvider}s to
     * validate credentials and return an authenticated user or throw an exception if authentication fails.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * A provider for generating and validating JWT (JSON Web Token) used for authenticating
     * and authorizing user requests within the application. It facilitates the creation
     * of secure tokens based on user authentication details and includes claims such as
     * roles and expiry information.
     */
    private final JwtTokenProvider tokenProvider;

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
     * Registers a new user in the system by saving the provided user details and returning
     * the unique identifier of the created user entity.
     *
     * @param userDTO the Data Transfer Object containing the details of the user to be registered
     * @return the unique {@link UUID} identifier of the newly created user
     */
    @Override
    public UUID register(final UserDTO userDTO) {
        final String hashedPassword = passwordEncoder.encode(userDTO.password());
        final User user = userRepository.save(userMapper.toEntity(userDTO, hashedPassword));
        return user.getId();
    }

    /**
     * Authenticates a user based on the provided credentials and generates
     * a JSON Web Token (JWT) for later requests.
     *
     * @param userDTO the Data Transfer Object containing the user's username and password
     * @return a signed JWT as a String, which can be used for authentication and authorization
     * in later requests
     */
    @Override
    public String login(final UserDTO userDTO) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDTO.username(),
                        userDTO.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.generateToken((UserPrincipal) authentication.getPrincipal());
    }
}