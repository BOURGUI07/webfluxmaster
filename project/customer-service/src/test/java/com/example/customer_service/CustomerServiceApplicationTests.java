package com.example.customer_service;

import com.example.customer_service.dto.CustomerInformation;
import com.example.customer_service.dto.Holding;
import com.example.customer_service.dto.TradeRequest;
import com.example.customer_service.dto.TradeResponse;
import com.example.customer_service.entity.Ticker;
import com.example.customer_service.entity.TickerAction;
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

@SpringBootTest
@AutoConfigureWebTestClient
class CustomerServiceApplicationTests {

	@Autowired
	private WebTestClient client;

	public WebTestClient.ResponseSpec getCustomer(Integer customerId, HttpStatus expectedStatus){
		return client.get()
				.uri("/customers/{customerId}", customerId)
				.exchange()
				.expectStatus().isEqualTo(expectedStatus);
	}

	public WebTestClient.ResponseSpec trade(Integer customerId, TradeRequest request, HttpStatus expectedStatus){
		return client.post()
				.uri("/customers/{customerId}/trade", customerId)
				.bodyValue(request)
				.exchange()
				.expectStatus().isEqualTo(expectedStatus);
	}

	@Test
	void testCustomerFound(){
		getCustomer(1,HttpStatus.OK)
				.returnResult(CustomerInformation.class)
				.getResponseBody()
				.as(StepVerifier::create)
				.assertNext(customerInformation -> {
					assertEquals("Sam",customerInformation.name());
					assertEquals(10000,customerInformation.balance());
					assertTrue(customerInformation.holdings().isEmpty());
				})
				.verifyComplete();

		getCustomer(2,HttpStatus.OK)
				.returnResult(CustomerInformation.class)
				.getResponseBody()
				.as(StepVerifier::create)
				.assertNext(customerInformation -> {
					assertEquals("Mike",customerInformation.name());
					assertEquals(10000,customerInformation.balance());
					assertTrue(customerInformation.holdings().isEmpty());
				})
				.verifyComplete();

		getCustomer(3,HttpStatus.OK)
				.returnResult(CustomerInformation.class)
				.getResponseBody()
				.as(StepVerifier::create)
				.assertNext(customerInformation -> {
					assertEquals("John",customerInformation.name());
					assertEquals(10000,customerInformation.balance());
					assertTrue(customerInformation.holdings().isEmpty());
				})
				.verifyComplete();
	}

	@Test
	void testNotFound(){
		getCustomer(4,HttpStatus.NOT_FOUND)
		.returnResult(ProblemDetail.class)
				.getResponseBody()
				.as(StepVerifier::create)
				.assertNext(problemDetail -> {
					assertEquals(HttpStatus.NOT_FOUND.value(),problemDetail.getStatus());
					assertEquals("Customer Not Found",problemDetail.getTitle());
					assertEquals("Customer with id 4 not found",problemDetail.getDetail());
				})
				.verifyComplete();
	}

	@Test
	void testBuyAndSellRequest(){
		var buyRequest = TradeRequest.builder()
				.price(10)
				.quantity(100)
				.ticker(Ticker.AMAZON)
				.tickerAction(TickerAction.BUY)
				.build();

		trade(1,buyRequest,HttpStatus.OK)
		.returnResult(TradeResponse.class)
				.getResponseBody()
				.as(StepVerifier::create)
				.assertNext(tradeResponse -> {
					assertEquals(10,tradeResponse.price());
					assertEquals(100,tradeResponse.quantity());
					assertEquals(Ticker.AMAZON,tradeResponse.ticker());
					assertEquals(TickerAction.BUY,tradeResponse.tickerAction());
					assertEquals(9000,tradeResponse.balance());
					assertEquals(1,tradeResponse.customerId());
				})
				.verifyComplete();



		getCustomer(1,HttpStatus.OK)
		.returnResult(CustomerInformation.class)
				.getResponseBody()
				.as(StepVerifier::create)
				.assertNext(customerInformation -> {
					assertEquals("Sam",customerInformation.name());
					assertEquals(9000,customerInformation.balance());
                    assertEquals(1, customerInformation.holdings().size());
					assertEquals(Holding.builder()
							.ticker(Ticker.AMAZON)
							.quantity(100)
							.build(),customerInformation.holdings().getFirst());
				})
				.verifyComplete();

		var sellRequest = TradeRequest.builder()
				.price(10)
				.quantity(100)
				.ticker(Ticker.AMAZON)
				.tickerAction(TickerAction.SELL)
				.build();

		trade(1,sellRequest,HttpStatus.OK)
				.returnResult(TradeResponse.class)
				.getResponseBody()
				.as(StepVerifier::create)
				.assertNext(tradeResponse -> {
					assertEquals(10,tradeResponse.price());
					assertEquals(100,tradeResponse.quantity());
					assertEquals(Ticker.AMAZON,tradeResponse.ticker());
					assertEquals(TickerAction.SELL,tradeResponse.tickerAction());
					assertEquals(10000,tradeResponse.balance());
					assertEquals(1,tradeResponse.customerId());
				})
				.verifyComplete();



		getCustomer(1,HttpStatus.OK)
				.returnResult(CustomerInformation.class)
				.getResponseBody()
				.as(StepVerifier::create)
				.assertNext(customerInformation -> {
					assertEquals("Sam",customerInformation.name());
					assertEquals(10000,customerInformation.balance());
					assertEquals(1, customerInformation.holdings().size());
					assertEquals(Holding.builder()
							.ticker(Ticker.AMAZON)
							.quantity(0)
							.build(),customerInformation.holdings().getFirst());
				})
				.verifyComplete();
	}

	@Test
	void testNotEnoughBalanceForBuyRequest(){
		var buyRequest = TradeRequest.builder()
				.price(100)
				.quantity(1000)
				.ticker(Ticker.AMAZON)
				.tickerAction(TickerAction.BUY)
				.build();

		trade(1,buyRequest,HttpStatus.BAD_REQUEST)
				.returnResult(ProblemDetail.class)
				.getResponseBody()
				.as(StepVerifier::create)
				.assertNext(problemDetail -> {
					assertEquals(HttpStatus.BAD_REQUEST.value(),problemDetail.getStatus());
					assertEquals("Not Enough Balance",problemDetail.getTitle());
					assertEquals("Customer 1 does not have enough balance",problemDetail.getDetail());
				})
				.verifyComplete();
	}

	@Test
	void testNotEnoughSharesForSellRequest(){
		var sellRequest = TradeRequest.builder()
				.price(10)
				.quantity(100)
				.ticker(Ticker.AMAZON)
				.tickerAction(TickerAction.SELL)
				.build();

		trade(1,sellRequest,HttpStatus.BAD_REQUEST)
				.returnResult(ProblemDetail.class)
				.getResponseBody()
				.as(StepVerifier::create)
				.assertNext(problemDetail -> {
					assertEquals(HttpStatus.BAD_REQUEST.value(),problemDetail.getStatus());
					assertEquals("Not Enough Shares",problemDetail.getTitle());
					assertEquals("Customer 1 does not have enough shares",problemDetail.getDetail());
				})
				.verifyComplete();
	}

}
