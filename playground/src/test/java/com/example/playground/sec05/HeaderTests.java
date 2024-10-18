package com.example.playground.sec05;


import com.example.playground.sec05.dto.Product;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.Map;

public class HeaderTests extends AbstractWebClient {
    private WebClient client = createWebClient(b->b.defaultHeader("caller-id","order-service"));

    @Test
    void defaultHeader(){
        /*
            Provide product details for the given product id between 1 and 100.
             This header "caller-id" should be present. Example: "caller-id: order-service"
         */
        client
                .get()
                .uri("/lec04/product/{id}",1)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .verifyComplete();

    }

    @Test
    void overrideHeader(){
        client
                .get()
                .uri("/lec04/product/{id}",1)
                .header("caller-id","new-value")
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .verifyComplete();

    }

    @Test
    void headerMap(){
        var map = Map.of("caller-id","new-value",
                "some-key","some-value");
        client
                .get()
                .uri("/lec04/product/{id}",1)
                .headers(h->h.setAll(map))
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .verifyComplete();

    }
}
