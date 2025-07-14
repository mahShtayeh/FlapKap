package com.flapkap.vendingmachine.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Represents the response returned after a successful deposit operation.
 *
 * @param currentBalance The updated balance after the deposit transaction is completed.
 *                       This indicates the total amount of funds currently available for the user.
 */
@Schema(description = "Deposit response")
@Builder
public record DepositResponse(
        @Schema(description = "Updated balance", example = "1000")
        Integer currentBalance
) {
}