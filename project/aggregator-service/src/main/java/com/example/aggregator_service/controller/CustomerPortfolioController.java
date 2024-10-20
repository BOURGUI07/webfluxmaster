package com.example.aggregator_service.controller;

import com.example.aggregator_service.dto.CustomerInformation;
import com.example.aggregator_service.dto.StockTradeResponse;
import com.example.aggregator_service.dto.TradeRequest;
import com.example.aggregator_service.service.CustomerPortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerPortfolioController {
    private final CustomerPortfolioService service;

    @GetMapping("/{customerId}")
    public Mono<CustomerInformation> getCustomerInformation(
            @PathVariable Integer customerId
    ) {
        return service.getCustomerInformation(customerId);
    }

    @PostMapping("/{customerId}/trade")
    public Mono<StockTradeResponse> trade(
            @PathVariable Integer customerId,
            @RequestBody Mono<TradeRequest> request
    ){
        return service.trade(customerId, request);
    }
}
