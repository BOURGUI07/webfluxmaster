package com.example.customer_service.repo;

import com.example.customer_service.entity.PortfolioItem;
import com.example.customer_service.entity.Ticker;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PortfolioItemRepo extends ReactiveCrudRepository<PortfolioItem, Integer> {
    Flux<PortfolioItem> findByCustomerId(Integer customerId);
    Mono<PortfolioItem> findByCustomerIdAndTicker(Integer customerId, Ticker ticker);
}
