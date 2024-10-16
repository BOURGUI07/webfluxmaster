package com.example.playground.sec01.controller;

import com.example.playground.sec01.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
@RequestMapping("/reactive")
public class ReactiveController {


    private WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:7070")
            .build();


    @GetMapping("/products")
    public Flux<ProductDTO> getProducts() {
        return webClient.get()
                .uri("/demo01/products")
                .retrieve()
                .bodyToFlux(ProductDTO.class)
                .doOnNext(p -> log.info("Product: {}", p));
    }

    @GetMapping(value = "/products/stream",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductDTO> getProductsStream() {
        return webClient.get()
                .uri("/demo01/products")
                .retrieve()
                .bodyToFlux(ProductDTO.class)
                .doOnNext(p -> log.info("Product: {}", p));
    }
}
