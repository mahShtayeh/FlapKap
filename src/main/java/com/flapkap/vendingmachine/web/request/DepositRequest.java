package com.flapkap.vendingmachine.web.request;

import com.flapkap.vendingmachine.validation.annotation.ValidCoins;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

/**
 * Represents a deposit request containing a list of coins to be deposited.
 * Each coin in the list must adhere to specific validation constraints.
 * This request is used for deposit operations where users provide
 * valid coin values for transaction purposes.
 *
 * @author Mahmoud Shtayeh
 */
@Schema(description = "Deposit request")
@Builder
public record DepositRequest(
        @Schema(description = "Coins to be deposited", example = "[10, 50]")
        @Valid
        @NotEmpty(message = "error.deposit.emptyCoins")
        @ValidCoins(message = "error.transaction.invalidCoins")
        List<@NotNull(message = "error.deposit.nullCoin") Integer> coins
) {
}