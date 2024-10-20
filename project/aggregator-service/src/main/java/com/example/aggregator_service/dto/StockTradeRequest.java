package com.example.aggregator_service.dto;

import com.example.aggregator_service.domain.Ticker;
import com.example.aggregator_service.domain.TickerAction;
import lombok.Builder;

@Builder
public record StockTradeRequest(
        Ticker ticker,
        Integer price,
        Integer quantity,
        TickerAction tickerAction
) {
    public Integer totalPrice() {
        return price * quantity;
    }
}
