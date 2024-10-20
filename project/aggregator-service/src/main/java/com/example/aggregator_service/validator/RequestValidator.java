package com.example.aggregator_service.validator;

import com.example.aggregator_service.dto.TradeRequest;
import com.example.aggregator_service.exceptions.ApplicationExceptions;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class RequestValidator {

    public static Predicate<TradeRequest> validateQuantity(){
        return dto -> Objects.nonNull(dto.quantity()) && dto.quantity()>0;
    }

    public static Predicate<TradeRequest> validateTicker(){
        return dto -> Objects.nonNull(dto.quantity());
    }

    public static Predicate<TradeRequest> validateTickerAction(){
        return dto -> Objects.nonNull(dto.quantity());
    }

    public static UnaryOperator<Mono<TradeRequest>> validate(){
        return mono -> mono
                .filter(validateTicker())
                .switchIfEmpty(ApplicationExceptions.missingTicker())
                .filter(validateTickerAction())
                .switchIfEmpty(ApplicationExceptions.missingTickerAction())
                .filter(validateQuantity())
                .switchIfEmpty(ApplicationExceptions.invalidQuantity());
    }
}
