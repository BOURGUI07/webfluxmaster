package com.example.customer_service.dto;

import com.example.customer_service.entity.Ticker;
import lombok.Builder;

@Builder
public record Holding(
        Ticker ticker,
        Integer quantity
) {
}
