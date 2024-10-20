package com.example.aggregator_service.service;

import com.example.aggregator_service.client.CustomerServiceClient;
import com.example.aggregator_service.client.StockServiceClient;
import com.example.aggregator_service.dto.CustomerInformation;
import com.example.aggregator_service.dto.StockTradeResponse;
import com.example.aggregator_service.dto.TradeRequest;
import com.example.aggregator_service.mapper.Mapper;
import com.example.aggregator_service.validator.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerPortfolioService {
    private final StockServiceClient stockServiceClient;
    private final CustomerServiceClient customerServiceClient;

    public Mono<CustomerInformation> getCustomerInformation(Integer customerId) {
        return customerServiceClient.getCustomerInformation(customerId);
    }

    public Mono<StockTradeResponse> trade(Integer customerId, Mono<TradeRequest> tradeRequest) {
        return tradeRequest
                .transform(RequestValidator.validate())
                .zipWhen(x-> stockServiceClient.getStockPrice(x.ticker()))
                .map(y-> Mapper.toStockTradeRequest(y.getT2(),y.getT1()))
                .flatMap(x->customerServiceClient.trade(customerId, x));
    }
}
