package com.example.playground.sec02;

import com.example.playground.AbstractTest;
import com.example.playground.sec02.repo.CustomerOrderRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerOrderRepoTest extends AbstractTest {

    @Autowired
    private CustomerOrderRepo repo;

    @Test
    void findProductsByCustomerName() {
        repo.findByName("sam")
                .as(StepVerifier::create)
                .assertNext(products -> assertEquals("iphone 20", products.getDescription()))
                .assertNext(products -> assertEquals("iphone 18", products.getDescription()))
                .verifyComplete();
    }

    @Test
    void findOrderDetailsByProductDesc(){
        repo.findOrderDetailsByProduct("iphone 20")
                .as(StepVerifier::create)
                .assertNext(orderDetails -> assertEquals("mike",orderDetails.customerName()))
                .assertNext(orderDetails -> assertEquals("sam",orderDetails.customerName()))
                .verifyComplete();
    }
}
