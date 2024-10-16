package com.example.playground.sec02;

import com.example.playground.AbstractTest;
import com.example.playground.sec02.domain.Customer;
import com.example.playground.sec02.repo.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerRepoTest extends AbstractTest {

    @Autowired
    private CustomerRepo repo;

    @Test
    void findAll(){
        repo.findAll()
                .as(StepVerifier::create)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    void findById(){
        repo.findById(4)
                .as(StepVerifier::create)
                .assertNext(customer -> {
                    assertEquals("emily", customer.getName());
                    assertEquals("emily@example.com", customer.getEmail());
                })
                .verifyComplete();
    }

    @Test
    void findByIdEmpty(){
        repo.findById(25)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void findByName(){
        repo.findByName("liam")
                .as(StepVerifier::create)
                .assertNext(customer -> {
                    assertEquals(6, (int) customer.getId());
                    assertEquals("liam@example.com", customer.getEmail());
                })
                .verifyComplete();
    }

    @Test
    void findByEmailEndingWith(){
        repo.findByEmailEndingWith("gmail.com")
                .as(StepVerifier::create)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void insertAndDelete(){
        repo.save(new Customer().setName("youness").setEmail("youness@gmail.com"))
                .as(StepVerifier::create)
                .assertNext(customer -> assertNotNull(customer.getId()))
                .verifyComplete();

        repo.findAll().count()
                .as(StepVerifier::create)
                        .expectNext(11L)
                                .verifyComplete();

        repo.deleteById(11)
                .then(repo.deleteById(11))
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void update(){
        repo.findByName("ava")
                .doOnNext(customer -> customer.setName("cool"))
                .flatMap(customer -> repo.save(customer))
                .as(StepVerifier::create)
                .assertNext(customer -> assertEquals("cool", customer.getName()))
                .verifyComplete();
    }
}
