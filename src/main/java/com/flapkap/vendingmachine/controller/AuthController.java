package com.flapkap.vendingmachine.controller;

import com.flapkap.vendingmachine.mapper.UserMapper;
import com.flapkap.vendingmachine.service.UserService;
import com.flapkap.vendingmachine.web.RestResponse;
import com.flapkap.vendingmachine.web.request.LoginRequest;
import com.flapkap.vendingmachine.web.request.RegistrationRequest;
import com.flapkap.vendingmachine.web.response.LoginResponse;
import com.flapkap.vendingmachine.web.response.RegistrationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * The AuthController class provides RESTful API endpoints for user-related operations.
 * It is mapped to handle requests at the base path "/api/v1/users" with JSON request and response formats.
 * This controller relies on the UserService to perform business logic for user management.
 *
 * @author Mahmoud Shtayeh
 */
@Tag(name = "User Management", description = "Endpoints for user management")
@RestController
@RequiredArgsConstructor
@RequestMapping(
        path = "/api/v1/users",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
public class AuthController {
    /**
     * Service layer dependency responsible for handling business logic related to user operations.
     */
    private final UserService userService;

    /**
     * A dependency responsible for mapping between User domain objects
     * and their corresponding Data Transfer Objects (DTOs).
     */
    private final UserMapper userMapper;

    /**
     * Registers a new user in the system.
     *
     * @param request The registration request containing user details.
     *                Must be valid and not null.
     * @return A {@link RestResponse} containing
     * a {@link RegistrationResponse} object with the unique identifier of the registered user.
     */
    @SecurityRequirements()
    @Operation(summary = "Register a new user")
    @ApiResponse(responseCode = "201", description = "User registered successfully")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestResponse<RegistrationResponse> register(@Valid @RequestBody final RegistrationRequest request) {
        final UUID userId = userService.register(userMapper.toDTO(request));
        return RestResponse.ok(RegistrationResponse.builder()
                .userId(userId)
                .build());
    }

    /**
     * Authenticates a user and generates an access token upon successful login.
     *
     * @param request The login request containing the user's credentials (username and password).
     *                Must be valid and not null.
     * @return A {@link RestResponse} containing a {@link LoginResponse} object
     * with the generated authentication token.
     */
    @SecurityRequirements()
    @Operation(summary = "Login a user")
    @PostMapping("/logins")
    public RestResponse<LoginResponse> login(@Valid @RequestBody final LoginRequest request) {
        final String token = userService.login(userMapper.toDTO(request));
        return RestResponse.ok(LoginResponse.builder()
                .token(token)
                .build());
    }
}
