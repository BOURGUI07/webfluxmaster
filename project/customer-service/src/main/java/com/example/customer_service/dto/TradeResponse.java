package com.example.customer_service.dto;

import com.example.customer_service.entity.Ticker;
import com.example.customer_service.entity.TickerAction;
import lombok.Builder;

@Builder
public record TradeResponse(
        Integer customerId,
        Ticker ticker,
        Integer quantity,
        Integer price,
        Integer totalPrice,
        TickerAction tickerAction,
        Integer balance
) {
}
