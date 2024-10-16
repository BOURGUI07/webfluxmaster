package com.example.playground.sec02.repo;

import com.example.playground.sec02.domain.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepo extends ReactiveCrudRepository<Customer, Integer> {
    Mono<Customer> findByName(String name);
    Flux<Customer> findByEmailEndingWith(String text);
}
