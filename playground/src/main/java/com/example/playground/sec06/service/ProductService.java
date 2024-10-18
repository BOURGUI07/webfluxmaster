package com.example.playground.sec06.service;

import com.example.playground.sec06.dto.ProductRequest;
import com.example.playground.sec06.dto.ProductResponse;
import com.example.playground.sec06.mapper.ProductMapper;
import com.example.playground.sec06.repo.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo repo;

    public Flux<ProductResponse> saveProducts(Flux<ProductRequest> products) {
        return products.map(ProductMapper::toEntity)
                .as(repo::saveAll)
                .map(ProductMapper::toResponse);
    }

    public Mono<Long> getProductCount() {
        return repo.count();
    }
}
