package com.example.playground.sec06;

import com.example.playground.AbstractTest;
import com.example.playground.sec06.dto.ProductRequest;
import com.example.playground.sec06.dto.UploadResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class ProductClient extends AbstractTest {
    private WebClient client = WebClient.builder()
            .baseUrl("http://localhost:8080/products")
            .build();

    public Mono<UploadResponse> uploadProducts(Flux<ProductRequest> products){
        return client
                .post()
                .uri("/upload")
                .contentType(MediaType.APPLICATION_NDJSON)
                .body(products, ProductRequest.class)
                .retrieve()
                .bodyToMono(UploadResponse.class);
    }

    @Test
    void upload(){
        var flux = Flux.range(1,100)
                .map(x->ProductRequest.builder()
                        .description("iphone-"+x)
                        .price(x)
                        .build());

        uploadProducts(flux)
                .doOnNext(x-> log.info("RECEIVED RESPONSE: {}",x))
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
    }
}
