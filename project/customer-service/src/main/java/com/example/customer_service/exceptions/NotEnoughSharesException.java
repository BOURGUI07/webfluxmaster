package com.example.customer_service.exceptions;

public class NotEnoughSharesException extends RuntimeException {
    public NotEnoughSharesException(Integer customerId) {
        super("Customer " + customerId + " does not have enough shares");
    }
}
