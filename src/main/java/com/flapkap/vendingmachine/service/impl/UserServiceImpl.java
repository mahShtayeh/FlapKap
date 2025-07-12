package com.flapkap.vendingmachine.service.impl;

import com.flapkap.vendingmachine.dto.UserDTO;
import com.flapkap.vendingmachine.mapper.UserMapper;
import com.flapkap.vendingmachine.repository.UserRepository;
import com.flapkap.vendingmachine.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.flapkap.vendingmachine.model.User;

import java.util.UUID;

/**
 * Implementation of the {@link UserService} interface responsible for handling
 * business logic related to {@link User} entities.
 *
 * @author Mahmoud Shtayeh
 */
@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
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
}