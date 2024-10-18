package com.example.playground.sec07.controller;

import com.example.playground.sec07.dto.ProductRequest;
import com.example.playground.sec07.dto.ProductResponse;
import com.example.playground.sec07.dto.UploadResponse;
import com.example.playground.sec07.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService service;

    @PostMapping()
    public Mono<ProductResponse> saveProduct(
            @RequestBody Mono<ProductRequest> requests
            ){
        return service.saveProduct(requests);
    }

    @GetMapping(value ="/stream/{maxPrice}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductResponse> getProductStream(
            @PathVariable Integer maxPrice
    ) {
        return service.productStream(maxPrice);
    }


}
