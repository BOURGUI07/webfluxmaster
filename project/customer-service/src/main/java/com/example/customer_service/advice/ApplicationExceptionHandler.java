package com.example.customer_service.advice;

import com.example.customer_service.exceptions.CustomerNotFoundException;
import com.example.customer_service.exceptions.NotEnoughBalanceException;
import com.example.customer_service.exceptions.NotEnoughSharesException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;
import java.util.function.Consumer;

@ControllerAdvice
public class ApplicationExceptionHandler {

    public ProblemDetail handleProblem(Exception ex, HttpStatus status, Consumer<ProblemDetail> problemDetailConsumer) {
        var problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        problemDetailConsumer.accept(problemDetail);
        return problemDetail;
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail handleNotFoundException(CustomerNotFoundException ex){
        return handleProblem(ex, HttpStatus.NOT_FOUND,
                problemDetail -> {
                    problemDetail.setTitle("Customer Not Found");
                    problemDetail.setType(URI.create("http://example.com/problems/customerNotFound"));
                });
    }

    @ExceptionHandler(NotEnoughBalanceException.class)
    public ProblemDetail handleNotEnoughBalance(NotEnoughBalanceException ex){
        return handleProblem(ex, HttpStatus.BAD_REQUEST,
                problemDetail -> {
                    problemDetail.setTitle("Not Enough Balance");
                    problemDetail.setType(URI.create("http://example.com/problems/notEnoughBalance"));
                });
    }

    @ExceptionHandler(NotEnoughSharesException.class)
    public ProblemDetail handleNotEnoughShares(NotEnoughSharesException ex){
        return handleProblem(ex, HttpStatus.BAD_REQUEST,
                problemDetail -> {
                    problemDetail.setTitle("Not Enough Shares");
                    problemDetail.setType(URI.create("http://example.com/problems/notEnoughShares"));
                });
    }
}
