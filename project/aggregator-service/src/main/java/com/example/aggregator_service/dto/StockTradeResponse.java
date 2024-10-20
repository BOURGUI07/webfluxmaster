package com.example.aggregator_service.dto;

import com.example.aggregator_service.domain.Ticker;
import com.example.aggregator_service.domain.TickerAction;
import lombok.Builder;

@Builder
public record StockTradeResponse(
        Integer customerId,
        Ticker ticker,
        Integer quantity,
        Integer price,
        Integer totalPrice,
        TickerAction tickerAction,
        Integer balance
) {
}
