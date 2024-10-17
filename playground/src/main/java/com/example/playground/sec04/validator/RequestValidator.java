package com.example.playground.sec04.validator;

import com.example.playground.sec04.dto.CustomerRequest;
import com.example.playground.sec04.exceptions.ApplicationExceptions;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class RequestValidator {
    public static UnaryOperator<Mono<CustomerRequest>> validate(){
        return dto -> dto
                .filter(hasName())
                .switchIfEmpty(ApplicationExceptions.missingName())
                .filter(hasEmail())
                .switchIfEmpty(ApplicationExceptions.invalidEmail());

    }

    public static Predicate<CustomerRequest> hasName(){
        return dto -> Objects.nonNull(dto.name());
    }

    public static Predicate<CustomerRequest> hasEmail(){
        return dto -> Objects.nonNull(dto.email()) && dto.email().contains("@");
    }
}
