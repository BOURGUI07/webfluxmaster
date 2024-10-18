package com.example.playground.sec07;

import com.example.playground.AbstractTest;
import com.example.playground.sec07.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureWebTestClient
public class ServerSentTest extends AbstractTest {

    @Autowired
    private WebTestClient client;

    @Test
    void testServerSent() {
        client
                .get()
                .uri("/products/stream/80")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .returnResult(ProductResponse.class)
                .getResponseBody()
                .collectList()
                .as(StepVerifier::create)
                .assertNext(list -> {
                    assertEquals(6,list.size());
                    assertTrue(list.stream().allMatch(x->x.price()<=800));
                })
                .verifyComplete();
    }
}
