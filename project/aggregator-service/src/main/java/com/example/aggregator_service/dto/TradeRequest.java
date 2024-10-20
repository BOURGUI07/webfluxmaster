package com.example.aggregator_service.dto;

import com.example.aggregator_service.domain.Ticker;
import com.example.aggregator_service.domain.TickerAction;
import lombok.Builder;

@Builder
public record TradeRequest(
        Ticker ticker,
        TickerAction tickerAction,
        Integer quantity
) {
}
