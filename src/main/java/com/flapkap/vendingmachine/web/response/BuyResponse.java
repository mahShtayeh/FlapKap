package com.flapkap.vendingmachine.web.response;

import com.flapkap.vendingmachine.util.CoinUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Schema(description = "Buy response")
@Builder
public record BuyResponse(
        @Schema(description = "Bought Products")
        List<ProductResponse> boughtProducts,

        @Schema(description = "Total spent")
        Integer totalSpent,

        @Schema(description = "Coin changes")
        List<CoinUtil.CoinChange> changes
) {
}