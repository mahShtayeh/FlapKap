package com.flapkap.vendingmachine.web;

import com.flapkap.vendingmachine.exception.ApiError;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * Rest API response wrapper
 *
 * @param <P> API payload type
 * @author Mahmoud Shtayeh
 */
@Schema(description = "REST API response")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RestResponse<P> {
    /**
     * The payload of the API response.
     */
    @Schema(description = "Response payload")
    private P payload;

    /**
     * List of errors that occurred during the processing of a request.
     * This can include validation errors, server errors, or other types of issues
     * encountered while handling the API request. Each error is represented by an
     * instance of the {@link ApiError} class,
     * containing details such as the error message
     * and the timestamp when the error occurred.
     */
    @Schema(description = "List of errors, if any")
    private List<ApiError> errors;

    /**
     * Represents the time taken for the execution of the API response.
     */
    @Schema(description = "Execution time of the API", example = "1ms")
    private String executionTime;

    /**
     * Convenient method to create a valid response
     *
     * @param payload Rest API payload
     * @param <T>     Payload type
     * @return Rest API response
     */
    public static <T> RestResponse<T> ok(final T payload) {
        return RestResponse.<T>builder()
                .payload(payload)
                .build();
    }

    /**
     * Convenient method to create a response out of a single error
     *
     * @param error Rest API error
     * @param <T>   Payload type
     * @return Rest API response
     */
    public static <T> RestResponse<T> error(final ApiError error) {
        return RestResponse.<T>builder()
                .errors(List.of(error))
                .build();
    }
}