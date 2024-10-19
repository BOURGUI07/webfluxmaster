package com.example.customer_service.exceptions;

import reactor.core.publisher.Mono;

public class ApplicationExceptions {

    public static <T> Mono<T> customerNotFound(Integer customerId) {
        return Mono.error(new CustomerNotFoundException(customerId));
    }

    public static <T> Mono<T> notEnoughBalance(Integer customerId) {
        return Mono.error(new NotEnoughBalanceException(customerId));
    }

    public static <T> Mono<T> notEnoughShares(Integer customerId) {
        return Mono.error(new NotEnoughSharesException(customerId));
    }


}
