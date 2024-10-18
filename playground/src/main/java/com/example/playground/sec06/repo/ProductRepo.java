package com.example.playground.sec06.repo;

import com.example.playground.sec06.entity.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends ReactiveCrudRepository<Product, Integer> {
}
