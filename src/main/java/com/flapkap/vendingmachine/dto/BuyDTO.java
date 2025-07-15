package com.flapkap.vendingmachine.dto;

import com.flapkap.vendingmachine.util.CoinUtil;
import lombok.Builder;

import java.util.List;

@Builder
public record BuyDTO(
        List<ProductDTO> boughtProducts,
        Integer totalSpent,
        List<CoinUtil.CoinChange> changes
) {
}
