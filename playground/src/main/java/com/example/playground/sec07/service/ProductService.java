package com.example.playground.sec07.service;

import com.example.playground.sec07.dto.ProductRequest;
import com.example.playground.sec07.dto.ProductResponse;
import com.example.playground.sec07.mapper.ProductMapper;
import com.example.playground.sec07.repo.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo repo;
    private final Sinks.Many<ProductResponse> sink;

    public Mono<ProductResponse> saveProduct(Mono<ProductRequest> product) {
        return product.map(ProductMapper::toEntity)
                .flatMap(repo::save)
                .map(ProductMapper::toResponse)
                .doOnNext(sink::tryEmitNext);
    }

    public Flux<ProductResponse> productStream(Integer maxPrice) {
        return sink.asFlux()
                .filter(product -> product.price()<=maxPrice);
    }

}
