package com.example.aggregator_service.client;

import com.example.aggregator_service.dto.CustomerInformation;
import com.example.aggregator_service.dto.StockTradeRequest;
import com.example.aggregator_service.dto.StockTradeResponse;
import com.example.aggregator_service.exceptions.ApplicationExceptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
public class CustomerServiceClient {
    private final WebClient webClient;

    public Mono<CustomerInformation> getCustomerInformation(Integer customerId) {
        return webClient
                .get()
                .uri("/customers/{customerId}",customerId)
                .retrieve()
                .bodyToMono(CustomerInformation.class)
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> ApplicationExceptions.customerNotFound(customerId));
    }

    public Mono<StockTradeResponse> trade(Integer customerId, StockTradeRequest stockTradeRequest) {
        return webClient
                .post()
                .uri("/customers/{customerId}/trade",customerId)
                .bodyValue(stockTradeRequest)
                .retrieve()
                .bodyToMono(StockTradeResponse.class)
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> ApplicationExceptions.customerNotFound(customerId))
                .onErrorResume(WebClientResponseException.BadRequest.class, this::handleException);
    }

    public <T> Mono<T> handleException(WebClientResponseException.BadRequest exception){
        var problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
        var message = problemDetail!=null? problemDetail.getDetail(): exception.getMessage();
        log.info("PROBLEM WITH CUSTOMER SERVICE: {}", message);
        return ApplicationExceptions.invalidTradeRequest(message);
    }
}
