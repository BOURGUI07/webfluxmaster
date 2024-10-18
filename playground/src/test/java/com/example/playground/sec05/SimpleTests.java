package com.example.playground.sec05;

import com.example.playground.sec05.dto.Product;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SimpleTests extends AbstractWebClient{

    private final WebClient client = createWebClient();

    @Test
    void simpleGet() throws InterruptedException {
         client
                .get()
                .uri("/lec01/product/1")
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .subscribe();

         Thread.sleep(Duration.ofSeconds(2));
    }

    @Test
    void simpleGet1() throws InterruptedException {
        client
                .get()
                .uri("/{lec}/product/{id}","lec01",1)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .subscribe();

        Thread.sleep(Duration.ofSeconds(2));
    }

    @Test
    void simpleGet2() throws InterruptedException {
        var map = Map.of("id",1,"lec","lec01");
        client
                .get()
                .uri("/{lec}/product/{id}",map)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .subscribe();

        Thread.sleep(Duration.ofSeconds(2));
    }

    @Test
    void fluxGet() throws InterruptedException {
        client
                .get()
                .uri("/lec02/product/stream")
                .retrieve()
                .bodyToFlux(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .verifyComplete();

    }

    @Test
    void bodyValuePostRequest(){
        var product = Product.builder()
                .price(45)
                .description("cap")
                .build();

        client
                .post()
                .uri("/lec03/product")
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .as(StepVerifier::create)
                .assertNext(x-> {
                    assertNotNull(x.id());
                    assertEquals(45,x.price());
                    assertEquals("cap",x.description());
                })
                .verifyComplete();
    }

    @Test
    void bodyPostRequest(){
        var mono = Mono.fromSupplier(() ->Product.builder()
                .price(66)
                .description("cat")
                .build());

        client
                .post()
                .uri("/lec03/product")
                .body(mono,Product.class)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .as(StepVerifier::create)
                .assertNext(x-> {
                    assertNotNull(x.id());
                    assertEquals(66,x.price());
                    assertEquals("cat",x.description());
                })
                .verifyComplete();
    }




}
