package com.flapkap.vendingmachine.exception;

import com.flapkap.vendingmachine.web.RestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Global RestFull APIs exception advisor
 *
 * @author Mahmoud Shtayeh
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionAdvisor {
    /**
     * I18n supported messages source
     */
    private final MessageSource messageSource;

    /**
     * UnKnown exceptions handler
     *
     * @param exception UnKnown exception
     * @param <T>       the type of the payload in the {@link RestResponse}.
     * @return ApiResponse wrapping the error user-friendly details
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public <T> ResponseEntity<RestResponse<T>> handleException(final Exception exception) {
        final String message = messageSource
                .getMessage("error.unknown", null, LocaleContextHolder.getLocale());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(RestResponse.error(ApiError.builder()
                        .message(message)
                        .timestamp(LocalDateTime.now())
                        .build()));
    }

    /**
     * MethodArgumentNotValidException exception handler
     *
     * @param exception MethodArgumentNotValidException exception
     * @param <T>       the type of the payload in the {@link RestResponse}.
     * @return ApiResponse wrapping the error user-friendly details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public <T> ResponseEntity<RestResponse<T>> handleValidationException(final MethodArgumentNotValidException exception) {
        final List<ApiError> apiErrors = new ArrayList<>();
        exception.getBindingResult().getAllErrors().forEach(apiError -> apiErrors.add(ApiError.builder()
                .message(messageSource.getMessage(Objects.requireNonNull(apiError.getDefaultMessage()),
                        null, LocaleContextHolder.getLocale()))
                .timestamp(LocalDateTime.now())
                .build()));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(RestResponse.<T>builder()
                        .errors(apiErrors)
                        .build());
    }

    /**
     * Handles exceptions of type {@code UsernameNotFoundException} that indicates
     * a username could not be found. This method constructs an error response with
     * a user-friendly error message and returns it with a {@code NOT_FOUND} status code.
     *
     * @param exception the {@code UsernameNotFoundException} thrown when a username is not found
     * @param <T>       the type of the payload in the {@link RestResponse}
     * @return a {@link ResponseEntity} containing the {@link RestResponse} with an
     * error message and additional error details wrapped in {@link ApiError}
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public <T> ResponseEntity<RestResponse<T>> handleException(final UsernameNotFoundException exception) {
        final String message = messageSource
                .getMessage(exception.getMessage(), null, LocaleContextHolder.getLocale());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(RestResponse.error(ApiError.builder()
                        .message(message)
                        .timestamp(LocalDateTime.now())
                        .build()));
    }

    /**
     * Handles exceptions of type {@code UserNotFoundException} and constructs an error response
     * with a user-friendly error message. The response is sent with a {@code HTTP 404 NOT_FOUND} status code.
     *
     * @param exception the {@code UserNotFoundException} that indicates a user could not be found
     *                  and contains the relevant user identifier and error message details
     * @param <T>       the type of the payload in the {@link RestResponse}
     * @return a {@link ResponseEntity} containing the {@link RestResponse} with an error message
     * and additional error details wrapped in {@link ApiError}
     */
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public <T> ResponseEntity<RestResponse<T>> handleException(final UserNotFoundException exception) {
        final String message = messageSource
                .getMessage(exception.getMessage(), null, LocaleContextHolder.getLocale());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(RestResponse.error(ApiError.builder()
                        .message(MessageFormat.format(message, exception.getUserId()))
                        .timestamp(LocalDateTime.now())
                        .build()));
    }

    /**
     * Handles exceptions of type {@code AuthenticationException} and constructs
     * an error response with a user-friendly error message. The response is sent
     * with an {@code HTTP 401 UNAUTHORIZED} status code.
     *
     * @param exception the {@code AuthenticationException} thrown during an authentication failure
     * @param <T>       the type of the payload in the {@link RestResponse}
     * @return a {@link ResponseEntity} containing the {@link RestResponse} with an error
     * message and additional error details wrapped in {@link ApiError}
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public <T> ResponseEntity<RestResponse<T>> handleException(final AuthenticationException exception) {
        final String message = messageSource
                .getMessage("error.jwt.authentication", null, LocaleContextHolder.getLocale());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(RestResponse.error(ApiError.builder()
                        .message(message)
                        .timestamp(LocalDateTime.now())
                        .build()));
    }

    /**
     * Handles exceptions of type {@code AccessDeniedException} and constructs
     * an error response with a user-friendly error message. The response is sent
     * with an {@code HTTP 401 UNAUTHORIZED} status code.
     *
     * @param exception the {@code AccessDeniedException} thrown when access to a resource is denied
     * @param <T>       the type of the payload in the {@link RestResponse}
     * @return a {@link ResponseEntity} containing the {@link RestResponse} with an error
     * message and additional error details wrapped in {@link ApiError}
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public <T> ResponseEntity<RestResponse<T>> handleException(final AccessDeniedException exception) {
        final String message = messageSource
                .getMessage("error.jwt.access", null, LocaleContextHolder.getLocale());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(RestResponse.error(ApiError.builder()
                        .message(message)
                        .timestamp(LocalDateTime.now())
                        .build()));
    }

    /**
     * Handles exceptions of type {@code JwtAuthenticationException} and constructs an error response
     * with a user-friendly error message. The response is sent with an {@code HTTP 401 UNAUTHORIZED}
     * status code.
     *
     * @param exception the {@code JwtAuthenticationException} thrown during an authentication failure
     * @param <T>       the type of the payload in the {@link RestResponse}
     * @return a {@link ResponseEntity} containing the {@link RestResponse} with an error message and
     * additional error details wrapped in {@link ApiError}
     */
    @ExceptionHandler(JwtAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public <T> ResponseEntity<RestResponse<T>> handleException(final JwtAuthenticationException exception) {
        final String message = messageSource
                .getMessage(exception.getMessage(), null, LocaleContextHolder.getLocale());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(RestResponse.error(ApiError.builder()
                        .message(message)
                        .timestamp(LocalDateTime.now())
                        .build()));
    }
}