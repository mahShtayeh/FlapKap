package com.flapkap.vendingmachine.service;

import com.flapkap.vendingmachine.dto.UserDTO;
import com.flapkap.vendingmachine.mapper.UserMapper;
import com.flapkap.vendingmachine.model.Role;
import com.flapkap.vendingmachine.model.User;
import com.flapkap.vendingmachine.repository.UserRepository;
import com.flapkap.vendingmachine.security.JwtTokenProvider;
import com.flapkap.vendingmachine.security.UserPrincipal;
import com.flapkap.vendingmachine.service.impl.UserServiceImpl;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for the {@link UserServiceImpl} implementation.
 * This class verifies the correct behavior of the {@link UserServiceImpl} methods
 * and ensures that the business logic operates as expected.
 * It uses Mockito annotations for mocking dependencies and JUnit 5 for testing.
 *
 * @author Mahmoud Shtayeh
 */
@NoArgsConstructor
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    /**
     * A mocked instance of {@link UserRepository} used for testing purposes.
     * This mock is intended to simulate the behavior of the real {@link UserRepository}
     * interface, allowing tests to verify the integration points and logic of the
     * service layer without reliance on the actual persistence layer.
     */
    @Mock
    private UserRepository userRepository;

    /**
     * A mocked instance of {@link UserMapper} used for testing purposes.
     */
    @Mock
    private UserMapper userMapper;

    /**
     * Mock implementation of the {@link PasswordEncoder} interface used for
     * encoding passwords during unit tests.
     */
    @Mock
    private PasswordEncoder passwordEncoder;

    /**
     * A mock instance of {@link AuthenticationManager} used in unit tests to simulate the behavior
     * of the authentication process within the Spring Security framework.
     */
    @Mock
    private AuthenticationManager authenticationManager;

    /**
     * A mock instance of {@link JwtTokenProvider} used in unit tests to simulate
     * the behavior of the real {@link JwtTokenProvider} without invoking its actual functionality.
     * This allows testing of components that interact with the token provider,
     * such as generating or validating JSON Web Tokens (JWT), without relying on the actual implementation.
     */
    @Mock
    private JwtTokenProvider tokenProvider;

    /**
     * Mocked instance of the {@link Authentication} interface used for testing purposes in the
     * {@code UserServiceTest} class. The {@code Authentication} object represents the authentication
     * token for a user, typically including user credentials and granted authorities.
     */
    @Mock
    private Authentication authentication;

    /**
     * An instance of {@link UserServiceImpl} injected into the test class to perform
     * unit tests for the service layer. It is initialized and managed by Mockito's
     *
     * @InjectMocks annotation, which automatically injects the required mocked dependencies
     * (e.g., {@link UserRepository}, {@link UserMapper}) into the service implementation.
     */
    @InjectMocks
    private UserServiceImpl userService;

    /**
     * A unique identifier used to represent a specific user throughout the test cases.
     * It is initialized with a randomly generated UUID value to mimic the behavior of unique
     * user identification in a real-world scenario. This constant is primarily used to
     * validate methods that involve user identity management, such as registration or retrieval.
     */
    private static final UUID BUYER_ID = UUID.randomUUID();

    /**
     * A constant UUID used as a placeholder or identifier for test cases involving a seller entity.
     * This value represents a unique identifier specifically for scenarios where seller-related
     * interactions or validations are required in the context of the `UserServiceTest` class.
     */
    private static final UUID SELLER_ID = UUID.randomUUID();

    /**
     * Represents the default email address used in test cases for the {@link UserServiceTest} class.
     * This email mimics a user's email attribute and is used to validate scenarios such as
     * user registration or data mapping.
     */
    private static final String BUYER_USERNAME = "jack.smith@flapkap.com";

    /**
     * Represents a predefined username used to simulate or test the functionality
     * of a seller entity within the scope of the UserServiceTest. This value serves
     * as a static reference for scenarios where an authenticated seller's username
     * is required, ensuring consistency across test cases.
     */
    private static final String SELLER_USERNAME = "alen.martin@flapkap.com";

    /**
     * Represents a static initial test balance value for use in test cases.
     * This balance is used to initialize or validate functionality related
     * to monetary computations or operations involving user deposits within
     * the system.
     */
    private static final BigDecimal BALANCE = BigDecimal.valueOf(1000.00);

    /**
     * A constant representing a pre-defined password value, likely used for testing purposes
     * within the {@code UserServiceTest}. This password is stored as a hashed string,
     * emphasizing the importance of security when dealing with sensitive information.
     * SHA256 Hashed value of: <PASSWORD>.
     */
    private static final String HASHED_PASSWORD = "73e460e77a656f17dfe522a7571e2671ead0abf36c4184ef3dfba2d4affe8702";

    /**
     * A constant representing an encrypted password value used for testing or internal validation purposes.
     * This value is intended to simulate an encoded password similar to those generated by the application's
     * password encoding mechanism, such as BCrypt hashing, for validating password security features.
     */
    private static final String ENCRYPTED_PASSWORD = "$2a$12$xJZv6T6OrSg4KUYAYOjISuL/9bh4ZhcEFYEIqMAzHKLXsf2MZL15G";

    /**
     * A constant holding the value of a JSON Web Token (JWT) used for testing purposes
     * in the {@code UserServiceTest} class. This token can simulate authenticated interactions
     * during unit testing by representing authorization and user-related claims.
     */
    private static final String JWT_TOKEN = "<TOKEN>";

    /**
     * A predefined static instance of {@link UserDTO} used for testing purposes in the {@code UserServiceTest} class.
     * This instance is built using the {@link UserDTO#builder()} method and initialized with specific
     * values for the user's first name, last name, email, and password.
     */
    private static final UserDTO BUYER_DTO = UserDTO.builder()
            .username(BUYER_USERNAME)
            .password(HASHED_PASSWORD)
            .deposit(BALANCE)
            .role(Role.BUYER)
            .enabled(true)
            .build();

    /**
     * A static, immutable instance of the {@link User} entity used for transient operations
     * in testing within the {@link UserServiceTest} class. This instance is pre-configured with
     * predefined values for `firstName`, `lastName`, `email`, and `password` fields,
     * allowing consistent and reusable test execution.
     */
    private static final User TRANSIENT_BUYER = User.builder()
            .username(BUYER_USERNAME)
            .password(ENCRYPTED_PASSWORD)
            .deposit(BALANCE)
            .role(Role.BUYER)
            .enabled(true)
            .build();

    /**
     * A predefined instance of the {@link User} class for testing purposes in the `UserServiceTest` class.
     * This object is constructed using the builder pattern and represents a fully initialized user entity
     * with the specified test data such as `USER_ID`, `FIRST_NAME`, `LAST_NAME`, `EMAIL`, and `PASSWORD`.
     */
    private static final User BUYER = User.builder()
            .id(BUYER_ID)
            .username(BUYER_USERNAME)
            .password(ENCRYPTED_PASSWORD)
            .deposit(BALANCE)
            .role(Role.BUYER)
            .enabled(true)
            .build();

    /**
     * A constant representing a predefined {@link UserPrincipal} for a buyer user in the system.
     * This object is constructed using the builder pattern and includes essential authentication
     * and authorization details for a buyer role. It is statically defined for use in tests
     * to validate application logic involving buyer-specific actions.
     */
    private static final UserPrincipal BUYER_PRINCIPAL = UserPrincipal.builder()
            .id(BUYER_ID)
            .username(BUYER_USERNAME)
            .password(ENCRYPTED_PASSWORD)
            .authorities(Collections.singleton(new SimpleGrantedAuthority(Role.BUYER.name())))
            .enabled(true)
            .build();

    /**
     * A static final instance of the {@link UserDTO} class that represents a pre-configured seller user.
     * This instance is created using the builder pattern to define the seller's username, hashed password,
     * deposit balance, and role as a buyer. It is intended to be used in the context of tests within
     * the UserServiceTest class.
     */
    private static final UserDTO SELLER_DTO = UserDTO.builder()
            .username(SELLER_USERNAME)
            .password(HASHED_PASSWORD)
            .deposit(BALANCE)
            .role(Role.BUYER)
            .enabled(true)
            .build();

    /**
     * Represents a static, pre-configured `User` instance used as a test data fixture
     * for representing a transient seller within the `UserServiceTest` test class.
     * This user object is initialized with a username, encrypted password, a balance,
     * and the `SELLER` role. It is primarily used to validate functionalities
     * involving seller user scenarios in unit tests.
     */
    private static final User TRANSIENT_SELLER = User.builder()
            .username(BUYER_USERNAME)
            .password(ENCRYPTED_PASSWORD)
            .deposit(BALANCE)
            .role(Role.SELLER)
            .enabled(true)
            .build();

    /**
     * Represents a predefined static instance of the {@link User} class used within the test context.
     * The `SELLER` variable is configured with specific attributes for test scenarios related to
     * operations involving a user with the role of `SELLER`.
     */
    private static final User SELLER = User.builder()
            .id(SELLER_ID)
            .username(BUYER_USERNAME)
            .password(ENCRYPTED_PASSWORD)
            .deposit(BALANCE)
            .role(Role.BUYER)
            .enabled(true)
            .build();

    /**
     * Represents the {@link UserPrincipal} object used to define the security context
     * and attributes of a seller user within the system during authentication and authorization processes.
     * This principal encapsulates the necessary credentials and attributes assigned specifically to users
     * with the {@code SELLER} role.
     */
    private static final UserPrincipal SELLER_PRINCIPAL = UserPrincipal.builder()
            .id(SELLER_ID)
            .username(SELLER_USERNAME)
            .password(ENCRYPTED_PASSWORD)
            .authorities(Collections.singleton(new SimpleGrantedAuthority(Role.SELLER.name())))
            .enabled(true)
            .build();

    /**
     * Test class for validating the registration functionality in the `UserService` class.
     *
     * @author Mahmoud Shtayeh
     */
    @Nested
    @NoArgsConstructor
    class RegistrationTests {
        /**
         * Validates the functionality of the `register` method in the `UserService` class
         * when registering a seller with valid details.
         */
        @Test
        void registerSeller_withValidDetails_shouldReturnsUserId() {
            when(passwordEncoder.encode(HASHED_PASSWORD)).thenReturn(ENCRYPTED_PASSWORD);
            when(userMapper.toEntity(SELLER_DTO, ENCRYPTED_PASSWORD)).thenReturn(TRANSIENT_SELLER);
            when(userRepository.save(TRANSIENT_SELLER)).thenReturn(SELLER);

            final UUID userId = userService.register(SELLER_DTO);

            verify(userMapper).toEntity(argThat(userDTO -> userDTO.equals(SELLER_DTO)),
                    argThat(ENCRYPTED_PASSWORD::equals));
            verify(userRepository).save(TRANSIENT_SELLER);
            assertThat(userId).isEqualTo(SELLER_ID);
        }

        /**
         * Validates the functionality of the `register` method in the `UserService` class
         * when registering a valid user by ensuring the proper interactions with `userMapper`
         * and `userRepository` and verifying the expected user ID is returned.
         */
        @Test
        void registerBuyer_withValidDetails_shouldReturnsUserId() {
            when(passwordEncoder.encode(HASHED_PASSWORD)).thenReturn(ENCRYPTED_PASSWORD);
            when(userMapper.toEntity(BUYER_DTO, ENCRYPTED_PASSWORD)).thenReturn(TRANSIENT_BUYER);
            when(userRepository.save(TRANSIENT_BUYER)).thenReturn(BUYER);

            final UUID userId = userService.register(BUYER_DTO);

            verify(userMapper).toEntity(argThat(userDTO -> userDTO.equals(BUYER_DTO)),
                    argThat(ENCRYPTED_PASSWORD::equals));
            verify(userRepository).save(TRANSIENT_BUYER);
            assertThat(userId).isEqualTo(BUYER_ID);
        }
    }

    /**
     * Test class for validating the login functionality in the `UserService` class.
     *
     * @author Mahmoud Shtayeh
     */
    @Nested
    @NoArgsConstructor
    class LoginTests {
        /**
         * Tests the login functionality of the `UserService` for a buyer with valid credentials.
         * This method verifies that the login process successfully generates a JSON Web Token (JWT)
         * when a buyer's correct username and password are provided.
         */
        @Test
        void loginBuyer_withValidDetails_shouldReturnsJwtToken() {
            when(authenticationManager.authenticate(any())).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(BUYER_PRINCIPAL);
            when(tokenProvider.generateToken(BUYER_PRINCIPAL)).thenReturn(JWT_TOKEN);

            final String jwtToken = userService.login(BUYER_DTO);

            assertThat(jwtToken).isNotNull();
            assertThat(jwtToken).isEqualTo(JWT_TOKEN);
            verify(authenticationManager).authenticate(
                    new UsernamePasswordAuthenticationToken(
                            BUYER_DTO.username(),
                            BUYER_DTO.password()
                    )
            );
        }

        /**
         * Tests the login functionality of the `UserService` for a seller with valid credentials.
         * This method verifies that the login process successfully generates a JSON Web Token (JWT)
         * when a seller's correct username and password are provided.
         */
        @Test
        void loginSeller_withValidDetails_shouldReturnsJwtToken() {
            when(authenticationManager.authenticate(any())).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(SELLER_PRINCIPAL);
            when(tokenProvider.generateToken(SELLER_PRINCIPAL)).thenReturn(JWT_TOKEN);

            final String jwtToken = userService.login(SELLER_DTO);

            assertThat(jwtToken).isNotNull();
            assertThat(jwtToken).isEqualTo(JWT_TOKEN);
            verify(authenticationManager).authenticate(
                    new UsernamePasswordAuthenticationToken(
                            SELLER_DTO.username(),
                            SELLER_DTO.password()
                    )
            );
        }
    }
}