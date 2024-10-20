package com.example.aggregator_service.mapper;

import com.example.aggregator_service.dto.StockPriceResponse;
import com.example.aggregator_service.dto.StockTradeRequest;
import com.example.aggregator_service.dto.TradeRequest;

public class Mapper {
    public static StockTradeRequest toStockTradeRequest(StockPriceResponse response, TradeRequest request) {
        return StockTradeRequest.builder()
                .price(response.price())
                .tickerAction(request.tickerAction())
                .ticker(response.ticker())
                .quantity(request.quantity())
                .build();
    }
}
