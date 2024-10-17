package com.example.playground.sec04.exceptions;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Integer customerId) {
        super("Customer with id " + customerId + " not found");
    }
}
