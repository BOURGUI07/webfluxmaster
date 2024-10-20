package com.example.aggregator_service;

import com.example.aggregator_service.domain.Ticker;
import com.example.aggregator_service.domain.TickerAction;
import com.example.aggregator_service.dto.CustomerInformation;
import com.example.aggregator_service.dto.StockTradeResponse;
import com.example.aggregator_service.dto.TradeRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureWebTestClient
@SpringBootTest
public class IntegrationTests{
    @Autowired
    protected WebTestClient client;


    @Test
    void validateCustomerInformation() {
        client
                .get()
                .uri("/customers/1")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(CustomerInformation.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(customerInformation -> {
                    assertEquals("Sam",customerInformation.name());
                    assertEquals(10000,customerInformation.balance());
                    assertTrue(customerInformation.holdings().isEmpty());
                })
                .verifyComplete();
    }

    @Test
    void notFoundCustomerInformation() {
        client
                .get()
                .uri("/customers/7")
                .exchange()
                .expectStatus()
                .isNotFound()
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(problemDetail -> {
                    assertEquals(404,problemDetail.getStatus());
                    assertEquals("Customer Not Found",problemDetail.getTitle());
                    assertEquals("Customer with id 7 not found",problemDetail.getDetail());
                })
                .verifyComplete();

    }

    @Test
    void buyAndSellTradeRequest(){
        var buyRequest = TradeRequest.builder()
                .tickerAction(TickerAction.BUY)
                .ticker(Ticker.GOOGLE)
                .quantity(10)
                .build();

        trade(HttpStatus.OK, buyRequest)
                .returnResult(StockTradeResponse.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(stockTradeResponse -> {
                    assertEquals(Ticker.GOOGLE,stockTradeResponse.ticker());
                    assertEquals(1,stockTradeResponse.customerId());
                    assertEquals(10,stockTradeResponse.quantity());
                    assertEquals(TickerAction.BUY,stockTradeResponse.tickerAction());
                    assertTrue(stockTradeResponse.balance()<10000);
                })
                .verifyComplete();

        var sellRequest = TradeRequest.builder()
                .tickerAction(TickerAction.SELL)
                .ticker(Ticker.GOOGLE)
                .quantity(10)
                .build();

        trade(HttpStatus.OK, sellRequest)
                .returnResult(StockTradeResponse.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(stockTradeResponse -> {
                    assertEquals(Ticker.GOOGLE,stockTradeResponse.ticker());
                    assertEquals(1,stockTradeResponse.customerId());
                    assertEquals(10,stockTradeResponse.quantity());
                    assertEquals(TickerAction.SELL,stockTradeResponse.tickerAction());
                })
                .verifyComplete();
    }

    @Test
    void missingTickerName(){
        var buyRequest = TradeRequest.builder()
                .tickerAction(TickerAction.BUY)
                .quantity(10)
                .build();
        trade(HttpStatus.BAD_REQUEST, buyRequest)
        .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(problemDetail -> {
                    assertEquals(400,problemDetail.getStatus());
                    assertEquals("Invalid trade request",problemDetail.getTitle());
                    assertEquals("Ticker is missing",problemDetail.getDetail());
                })
                .verifyComplete();
    }

    @Test
    void missingTickerAction(){
        var buyRequest = TradeRequest.builder()
                .ticker(Ticker.GOOGLE)
                .quantity(10)
                .build();
        trade(HttpStatus.BAD_REQUEST, buyRequest)
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(problemDetail -> {
                    assertEquals(400,problemDetail.getStatus());
                    assertEquals("Invalid trade request",problemDetail.getTitle());
                    assertEquals("Ticker Action is missing",problemDetail.getDetail());
                })
                .verifyComplete();
    }

    @Test
    void invalidQuantity(){
        var buyRequest = TradeRequest.builder()
                .ticker(Ticker.GOOGLE)
                .tickerAction(TickerAction.BUY)
                .quantity(0)
                .build();
        trade(HttpStatus.BAD_REQUEST, buyRequest)
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(problemDetail -> {
                    assertEquals(400,problemDetail.getStatus());
                    assertEquals("Invalid trade request",problemDetail.getTitle());
                    assertEquals("Quantity should be > 0",problemDetail.getDetail());
                })
                .verifyComplete();
    }

    @Test
    void notEnoughBalance(){
        var buyRequest = TradeRequest.builder()
                .ticker(Ticker.GOOGLE)
                .tickerAction(TickerAction.BUY)
                .quantity(20000)
                .build();
        trade(HttpStatus.BAD_REQUEST, buyRequest)
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(problemDetail -> {
                    assertEquals(400,problemDetail.getStatus());
                    assertEquals("Invalid trade request",problemDetail.getTitle());
                    assertEquals("Customer 1 does not have enough balance",problemDetail.getDetail());
                })
                .verifyComplete();
    }

    @Test
    void notEnoughShares(){
        var sellRequest = TradeRequest.builder()
                .ticker(Ticker.GOOGLE)
                .tickerAction(TickerAction.SELL)
                .quantity(400)
                .build();
        trade(HttpStatus.BAD_REQUEST, sellRequest)
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(problemDetail -> {
                    assertEquals(400,problemDetail.getStatus());
                    assertEquals("Invalid trade request",problemDetail.getTitle());
                    assertEquals("Customer 1 does not have enough shares",problemDetail.getDetail());
                })
                .verifyComplete();
    }

    public WebTestClient.ResponseSpec trade(HttpStatus status, TradeRequest tradeRequest){
        return client
                .post()
                .uri("/customers/1/trade")
                .bodyValue(tradeRequest)
                .exchange()
                .expectStatus().isEqualTo(status);
    }


}
