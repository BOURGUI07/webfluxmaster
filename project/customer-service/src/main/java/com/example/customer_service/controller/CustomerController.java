package com.example.customer_service.controller;

import com.example.customer_service.dto.CustomerInformation;
import com.example.customer_service.dto.TradeRequest;
import com.example.customer_service.dto.TradeResponse;
import com.example.customer_service.service.CustomerService;
import com.example.customer_service.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final TradeService tradeService;

    @GetMapping("/{customerId}")
    public Mono<CustomerInformation> getCustomerInformation(
            @PathVariable Integer customerId
    ) {
        return customerService.getCustomerInformation(customerId);
    }

    @PostMapping("/{customerId}/trade")
    public Mono<TradeResponse> trade(
            @PathVariable Integer customerId,
            @RequestBody Mono<TradeRequest> tradeRequest
    ){
        return tradeService.trade(customerId, tradeRequest);
    }
}
