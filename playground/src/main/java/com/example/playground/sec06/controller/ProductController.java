package com.example.playground.sec06.controller;

import com.example.playground.sec06.dto.ProductRequest;
import com.example.playground.sec06.dto.UploadResponse;
import com.example.playground.sec06.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService service;

    @PostMapping(value = "/upload",consumes = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<UploadResponse> uploadProducts(
            @RequestBody Flux<ProductRequest> requests
            ){
        log.info("Uploading products");
        return service.saveProducts(requests
                        .doOnNext(dto-> log.info("RECEIVED REQUEST: {}",dto)))
                .then(service.getProductCount())
                .map(x -> new UploadResponse(UUID.randomUUID(),x));
    }
}
