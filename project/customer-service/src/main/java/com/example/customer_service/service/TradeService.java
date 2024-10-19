package com.example.customer_service.service;

import com.example.customer_service.dto.TradeRequest;
import com.example.customer_service.dto.TradeResponse;
import com.example.customer_service.entity.Customer;
import com.example.customer_service.exceptions.ApplicationExceptions;
import com.example.customer_service.mapper.Mapper;
import com.example.customer_service.repo.CustomerRepo;
import com.example.customer_service.repo.PortfolioItemRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TradeService {
    private final CustomerRepo customerRepo;
    private final PortfolioItemRepo portfolioItemRepo;

    @Transactional
    public Mono<TradeResponse> trade(Integer customerId, Mono<TradeRequest> tradeRequest) {
        return tradeRequest
                .flatMap(request -> {
                    return switch (request.tickerAction()){
                        case SELL -> sellStock(customerId,request);
                        case BUY -> buyStock(customerId,request);
                    };
                });
    }

    private Mono<TradeResponse> buyStock(Integer customerId, TradeRequest tradeRequest) {
        return customerRepo.findById(customerId)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(customerId))
                .filter(x->x.getBalance()>= tradeRequest.totalPrice())
                .switchIfEmpty(ApplicationExceptions.notEnoughBalance(customerId))
                .flatMap(x-> executeBuyAction(x,tradeRequest));
    }

    private Mono<TradeResponse> executeBuyAction(Customer customer, TradeRequest tradeRequest) {
        var customerId = customer.getId();
        customer.setBalance(customer.getBalance()-tradeRequest.totalPrice());
        return portfolioItemRepo.findByCustomerIdAndTicker(customerId,tradeRequest.ticker())
                .defaultIfEmpty(Mapper.toPortfolioItem(customerId,tradeRequest.ticker()))
                .map(portfolioItem ->portfolioItem.setQuantity(portfolioItem.getQuantity()+tradeRequest.quantity()))
                .flatMap(portfolioItemRepo::save)
                .then(customerRepo.save(customer))
                .map(customerEntity-> Mapper.toTradeResponse(customerEntity,tradeRequest));

    }

    private Mono<TradeResponse> sellStock(Integer customerId, TradeRequest tradeRequest) {
        return customerRepo.findById(customerId)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(customerId))
                .zipWhen(y->portfolioItemRepo.findByCustomerIdAndTicker(y.getId(),tradeRequest.ticker())
                        .filter(x-> x.getQuantity()>= tradeRequest.quantity())
                        .switchIfEmpty(ApplicationExceptions.notEnoughShares(customerId))
                )
                .flatMap(x-> executeSellAction(x.getT1(),tradeRequest));
    }

    private Mono<TradeResponse> executeSellAction(Customer customer, TradeRequest tradeRequest) {
        var customerId = customer.getId();
        customer.setBalance(customer.getBalance()+tradeRequest.totalPrice());
        return portfolioItemRepo.findByCustomerIdAndTicker(customerId,tradeRequest.ticker())
                .map(portfolioItem ->portfolioItem.setQuantity(portfolioItem.getQuantity()-tradeRequest.quantity()))
                .flatMap(portfolioItemRepo::save)
                .then(customerRepo.save(customer))
                .map(customerEntity-> Mapper.toTradeResponse(customerEntity,tradeRequest));
    }
}
