package com.flapkap.vendingmachine.mapper;

import com.flapkap.vendingmachine.dto.UserDTO;
import com.flapkap.vendingmachine.model.User;
import com.flapkap.vendingmachine.web.request.RegistrationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * UserMapper is an interface that defines the mapping between User domain objects
 * and their corresponding Data Transfer Objects (DTOs). This interface is annotated
 * with @Mapper from MapStruct to facilitate the implementation of mapping logic.
 *
 * @author Mahmoud Shtayeh
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    /**
     * Maps a {@link RegistrationRequest} object to a {@link UserDTO}.
     *
     * @param request the registration request containing user details.
     * @return a {@link UserDTO} object representing the user details in a Data Transfer Object format
     */
    UserDTO toDTO(RegistrationRequest request);

    /**
     * Converts a {@link UserDTO} object to a {@link User} entity.
     *
     * @param userDTO the Data Transfer Object containing user details to be mapped to a User entity.
     * @return a {@link User} entity containing the mapped user details.
     */
    @Mapping(target = "password", source = "hashedPassword")
    User toEntity(UserDTO userDTO, String hashedPassword);
}