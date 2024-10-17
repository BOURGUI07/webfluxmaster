package com.example.playground.sec02;

import com.example.playground.AbstractTest;
import com.example.playground.sec03.dto.CustomerRequest;
import com.example.playground.sec03.dto.CustomerResponse;
import com.example.playground.sec03.dto.PaginatedCustomerResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureWebTestClient
public class CustomerControllerTest extends AbstractTest {

    @Autowired
    private WebTestClient client;

    @Test
    void findAll(){
        client.get()
                .uri("/customers")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CustomerResponse.class)
                .hasSize(10);
    }

    @Test
    void findAllPaginated(){
        client.get()
                .uri("/customers/paginated?page=1&size=2")
                .exchange()
                .expectStatus().isOk()
                .returnResult(PaginatedCustomerResponse.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(paginatedCustomerResponse -> {
                    assertEquals("sam", paginatedCustomerResponse.response().get(0).name());
                    assertEquals("mike", paginatedCustomerResponse.response().get(1).name());
                    assertEquals(10, (long) paginatedCustomerResponse.count());
                })
                .verifyComplete();

    }

    @Test
    void findById(){
        client.get()
                .uri("/customers/1")
                .exchange()
                .expectStatus().isOk()
                .returnResult(CustomerResponse.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(customerResponse -> {
                    assertEquals("sam",customerResponse.name());
                    assertEquals("sam@gmail.com",customerResponse.email());
                })
                .verifyComplete();

    }

    @Test
    void createAndUpdateAndDelete(){
        var request = new CustomerRequest("youness","youness@gmail.com");
        client
                .post()
                .uri("/customers")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .returnResult(CustomerResponse.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(customerResponse -> {
                    assertEquals(11,customerResponse.id());
                    assertEquals("youness",customerResponse.name());
                    assertEquals("youness@gmail.com",customerResponse.email());
                })
                .verifyComplete();

        var updateRequest = new CustomerRequest("sara","youness@gmail.com");
        client
                .put()
                .uri("/customers/11")
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus().isOk()
                .returnResult(CustomerResponse.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(customerResponse -> {
                    assertEquals(11,customerResponse.id());
                    assertEquals("sara",customerResponse.name());
                    assertEquals("youness@gmail.com",customerResponse.email());
                })
                .verifyComplete();

        client.delete()
                .uri("/customers/11")
                .exchange()
                .expectStatus().isOk();

    }


}
