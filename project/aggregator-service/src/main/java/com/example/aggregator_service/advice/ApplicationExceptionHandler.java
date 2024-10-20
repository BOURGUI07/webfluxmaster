package com.example.aggregator_service.advice;

import com.example.aggregator_service.exceptions.CustomerNotFoundException;
import com.example.aggregator_service.exceptions.InvalidTradeRequestException;
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

    @ExceptionHandler(InvalidTradeRequestException.class)
    public ProblemDetail handleInvalidTradeRequest(InvalidTradeRequestException ex){
        return handleProblem(ex, HttpStatus.BAD_REQUEST,
                problemDetail -> {
                    problemDetail.setTitle("Invalid trade Request");
                    problemDetail.setType(URI.create("http://example.com/problems/invalidTradeRequest"));
                });
    }

}
