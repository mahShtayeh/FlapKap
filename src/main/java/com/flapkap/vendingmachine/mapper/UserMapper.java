package com.flapkap.vendingmachine.mapper;

import com.flapkap.vendingmachine.dto.UserDTO;
import com.flapkap.vendingmachine.model.Role;
import com.flapkap.vendingmachine.model.User;
import com.flapkap.vendingmachine.security.UserPrincipal;
import com.flapkap.vendingmachine.web.request.LoginRequest;
import com.flapkap.vendingmachine.web.request.RegistrationRequest;
import org.mapstruct.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Set;

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
    @Mapping(target = "deposit", constant = "0")
    @Mapping(source = "enabled", target = "enabled", defaultValue = "true")
    UserDTO toDTO(RegistrationRequest request);

    /**
     * Converts a {@link UserDTO} object to a {@link User} entity.
     *
     * @param userDTO the Data Transfer Object containing user details to be mapped to a User entity.
     * @return a {@link User} entity containing the mapped user details.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", source = "hashedPassword")
    User toEntity(UserDTO userDTO, String hashedPassword);

    /**
     * Converts a {@link LoginRequest} object into a {@link UserDTO}.
     *
     * @param request the login request containing user credentials, such as username and password.
     * @return a {@link UserDTO} containing the mapped user details in a Data Transfer Object format.
     */
    UserDTO toDTO(LoginRequest request);

    /**
     * Maps a {@link User} entity to a {@link UserPrincipal}, which is used for authentication
     * and authorization in the application. This method converts the user details,
     * such as roles, into a {@link UserPrincipal} object implementing the {@link UserDetails} interface.
     *
     * @param user the {@link User} entity containing user details like roles, username, and password.
     * @return a {@link UserPrincipal} object that encapsulates the user's authentication and authorization information.
     */
    @Mapping(target = "authorities", expression = "java(toAuthorities(user.getRole()))")
    UserPrincipal toPrincipal(User user);

    /**
     * Converts a {@link Role} entity to a {@link SimpleGrantedAuthority} object.
     * The resulting authority is used for Spring Security authorization.
     *
     * @param role the {@link Role} entity representing a specific role type to be converted
     *             into a Spring Security {@link SimpleGrantedAuthority}. The role must have
     *             its 'name' field properly set and non-null.
     * @return a {@link SimpleGrantedAuthority} object with the role's name as its authority.
     */
    default Set<SimpleGrantedAuthority> toAuthorities(final Role role) {
        return Collections.singleton(new SimpleGrantedAuthority(MessageFormat.format("{0}{1}",
                UserPrincipal.ROLE_PREFIX, role.name())));
    }
}