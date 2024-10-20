package com.example.aggregator_service.dto;

import com.example.aggregator_service.domain.Ticker;
import lombok.Builder;

@Builder
public record Holding(
        Ticker ticker,
        Integer quantity
) {
}
