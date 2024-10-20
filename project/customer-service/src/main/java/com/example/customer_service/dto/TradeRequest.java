package com.example.customer_service.dto;

import com.example.customer_service.entity.Ticker;
import com.example.customer_service.entity.TickerAction;
import lombok.Builder;

@Builder
public record TradeRequest(
        Ticker ticker,
        Integer price,
        Integer quantity,
        TickerAction tickerAction
) {
}
