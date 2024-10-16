package com.example.playground.sec02;

import com.example.playground.AbstractTest;
import com.example.playground.sec02.domain.Product;
import com.example.playground.sec02.repo.ProductRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.test.StepVerifier;

public class ProductRepoTest extends AbstractTest {
    @Autowired
    private ProductRepo repo;

    @Test
    void findByPriceBetween() {
        repo.findByPriceBetween(400,1200)
                .as(StepVerifier::create)
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    void pageable(){
        repo.findBy(PageRequest.of(0,2))
                .as(StepVerifier::create)
                .expectNext(new Product(1,"iphone 20",1000))
                .expectNext(new Product(2,"iphone 18",750))
                .verifyComplete();
    }

    @Test
    void pageableWithSort(){
        repo.findBy(PageRequest.of(0,4).withSort(Sort.by("price").descending()))
                .as(StepVerifier::create)
                .expectNext(new Product(4,"mac pro",3000))
                .expectNext(new Product(8,"imac",2000))
                .expectNext(new Product(6,"macbook air",1200))
                .expectNext(new Product(1,"iphone 20",1000))
                .verifyComplete();
    }
}
