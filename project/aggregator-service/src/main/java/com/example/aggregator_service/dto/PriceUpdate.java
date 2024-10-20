package com.example.aggregator_service.dto;

import com.example.aggregator_service.domain.Ticker;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PriceUpdate(
        Ticker ticker,
        Integer price,
        LocalDateTime time
) {
}
