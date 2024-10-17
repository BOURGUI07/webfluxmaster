package com.example.playground.sec04.exceptions;

import com.example.playground.sec04.dto.CustomerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.UnaryOperator;

public class ApplicationExceptions {
    public static <T> Mono<T> customerNotFound(Integer id){
        return Mono.error(new CustomerNotFoundException(id));
    }

    public static <T> Mono<T> missingName(){
        return Mono.error(new InvalidInputException("Name is required"));
    }

    public static <T> Mono<T> invalidEmail(){
        return Mono.error(new InvalidInputException("Email is invalid"));
    }


}
