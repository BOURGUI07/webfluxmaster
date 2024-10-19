package com.example.customer_service.service;

import com.example.customer_service.dto.CustomerInformation;
import com.example.customer_service.dto.Holding;
import com.example.customer_service.entity.PortfolioItem;
import com.example.customer_service.exceptions.ApplicationExceptions;
import com.example.customer_service.repo.CustomerRepo;
import com.example.customer_service.repo.PortfolioItemRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepo customerRepo;
    private final PortfolioItemRepo portfolioItemRepo;

    public Mono<CustomerInformation> getCustomerInformation(Integer customerId) {
        var holding = portfolioItemRepo.findByCustomerId(customerId)
                .map(portfolioItem -> Holding.builder()
                        .ticker(portfolioItem.getTicker())
                        .quantity(portfolioItem.getQuantity())
                        .build()
                )
                .collectList();

        return customerRepo.findById(customerId)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(customerId))
                .zipWhen(x -> holding)
                .map(x-> {
                    var customer = x.getT1();
                    var holdings = x.getT2();
                    return CustomerInformation.builder()
                            .balance(customer.getBalance())
                            .name(customer.getName())
                            .holdings(holdings)
                            .customerId(customer.getId())
                            .build();
                });

    }
}
