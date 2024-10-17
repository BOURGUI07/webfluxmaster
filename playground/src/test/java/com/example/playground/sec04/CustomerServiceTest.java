package com.example.playground.sec04;

import com.example.playground.AbstractTest;
import com.example.playground.sec03.dto.CustomerRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureWebTestClient
public class CustomerServiceTest extends AbstractTest {

    @Autowired
    private WebTestClient client;

    @Test
    void testNotFound(){
        client
                .get()
                .uri("/customers/55")
                .exchange()
                .expectStatus().is4xxClientError()
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(problem ->{
                    assertEquals(HttpStatus.NOT_FOUND.value(),problem.getStatus());
                    assertEquals("Customer with id 55 not found",problem.getDetail());
                    assertEquals("Customer not found",problem.getTitle());
                })
                .verifyComplete();

        client
                .delete()
                .uri("/customers/55")
                .exchange()
                .expectStatus().is4xxClientError()
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(problem ->{
                    assertEquals(HttpStatus.NOT_FOUND.value(),problem.getStatus());
                    assertEquals("Customer with id 55 not found",problem.getDetail());
                    assertEquals("Customer not found",problem.getTitle());
                })
                .verifyComplete();

        var updateRequest = new CustomerRequest("sara","youness@gmail.com");
        client
                .put()
                .uri("/customers/55")
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus().is4xxClientError()
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(problem ->{
                    assertEquals(HttpStatus.NOT_FOUND.value(),problem.getStatus());
                    assertEquals("Customer with id 55 not found",problem.getDetail());
                    assertEquals("Customer not found",problem.getTitle());
                })
                .verifyComplete();
    }

    @Test
    void missingName(){
        var request = new CustomerRequest(null,"youness@gmail.com");
        client
                .post()
                .uri("/customers")
                .bodyValue(request)
                .exchange()
                .expectStatus().is4xxClientError()
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(problem ->{
                    assertEquals(HttpStatus.BAD_REQUEST.value(),problem.getStatus());
                    assertEquals("Name is required",problem.getDetail());
                    assertEquals("Invalid input",problem.getTitle());
                })
                .verifyComplete();

        client
                .put()
                .uri("/customers/5")
                .bodyValue(request)
                .exchange()
                .expectStatus().is4xxClientError()
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(problem ->{
                    assertEquals(HttpStatus.BAD_REQUEST.value(),problem.getStatus());
                    assertEquals("Name is required",problem.getDetail());
                    assertEquals("Invalid input",problem.getTitle());
                })
                .verifyComplete();
    }

    @Test
    void invalidEmail(){
        var request = new CustomerRequest("youness","younessgmail.com");
        client
                .post()
                .uri("/customers")
                .bodyValue(request)
                .exchange()
                .expectStatus().is4xxClientError()
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(problem ->{
                    assertEquals(HttpStatus.BAD_REQUEST.value(),problem.getStatus());
                    assertEquals("Email is invalid",problem.getDetail());
                    assertEquals("Invalid input",problem.getTitle());
                })
                .verifyComplete();

        client
                .put()
                .uri("/customers/8")
                .bodyValue(request)
                .exchange()
                .expectStatus().is4xxClientError()
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(problem ->{
                    assertEquals(HttpStatus.BAD_REQUEST.value(),problem.getStatus());
                    assertEquals("Email is invalid",problem.getDetail());
                    assertEquals("Invalid input",problem.getTitle());
                })
                .verifyComplete();
    }
}
