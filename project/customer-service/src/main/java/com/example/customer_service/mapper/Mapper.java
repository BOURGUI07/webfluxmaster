package com.example.customer_service.mapper;

import com.example.customer_service.dto.TradeRequest;
import com.example.customer_service.dto.TradeResponse;
import com.example.customer_service.entity.Customer;
import com.example.customer_service.entity.PortfolioItem;
import com.example.customer_service.entity.Ticker;

public class Mapper {
    public static TradeResponse toTradeResponse(Customer customer, TradeRequest tradeRequest) {
        return TradeResponse.builder()
                .totalPrice(tradeRequest.totalPrice())
                .price(tradeRequest.price())
                .balance(customer.getBalance())
                .customerId(customer.getId())
                .quantity(tradeRequest.quantity())
                .ticker(tradeRequest.ticker())
                .tickerAction(tradeRequest.tickerAction())
                .build();
    }

    public static PortfolioItem toPortfolioItem(Integer customerId, Ticker ticker) {
        return PortfolioItem.builder()
                .customerId(customerId)
                .ticker(ticker)
                .build();
    }
}
