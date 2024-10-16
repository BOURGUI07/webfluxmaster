package com.example.playground.sec02.repo;

import com.example.playground.sec02.domain.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepo extends ReactiveCrudRepository<Product, Integer> {
    Flux<Product> findByPriceBetween(Integer lower, Integer upper);
    Flux<Product> findBy(Pageable pageable);
}
