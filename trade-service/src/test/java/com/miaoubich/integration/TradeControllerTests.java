package com.miaoubich.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.miaoubich.dto.TradeEvent;
import com.miaoubich.dto.TradeResponse;
import com.miaoubich.service.TradeService;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
public class TradeControllerTests {

	// Micronaut's non blocking HTTP client injected directly into the test
	@Inject
	@Client("/")
	HttpClient client;
	
	@Inject
	TradeService tradeService;
	
	/*
	 * Mocronaut's dependency injection replaces the real TradeService bean
	 *  with the Mockito mock instance for this test suite
	*/
	@MockBean(TradeService.class)
	TradeService tradeService() {
		return Mockito.mock(TradeService.class);
	}
	
	@BeforeEach
	void setup() {
		Mockito.reset(tradeService);
	}
	
	/*
	 * 1. Health Check Test
	 * */
	@Test
	@DisplayName("GET /trades/health should return 200 OK with running message")
	void healthCheckTest() {
		// we use toBlocking() method to execute the HTTP request synchronously 
		HttpResponse<String> response = client.toBlocking().exchange(
				HttpRequest.GET("/trades/health"), String.class
				);
		
		assertEquals(HttpStatus.OK, response.getStatus());
		assertEquals("Trade Service is up and running!", response.getBody().orElse(null));
	}
	
	/*
	 * 2. Create Trade (POST /trades)
	 * */
	@Test
	@DisplayName("POST /trades should invoke tradeService.pendingTrade and return 200 OK")
	void createTradeTest() {
		TradeEvent tradeEvent = createSampeTradeEvent();
		doNothing().when(tradeService).pendingTrade(any(TradeEvent.class));
		
		HttpResponse<Void> response = client.toBlocking().exchange(
				HttpRequest.POST("/trades", tradeEvent), Void.class
				);
		
		assertEquals(HttpStatus.OK, response.getStatus());
		verify(tradeService).pendingTrade(any(TradeEvent.class));
	}

	private TradeEvent createSampeTradeEvent() {
		return new TradeEvent(
				"tradeId-sample",
				"userId-sample",
				"BTC-EUR",
				"BUY",
				new BigDecimal("5"),
				new BigDecimal("65000.00"),
				"Crypto",
				"PENDING",
				Instant.now()
				);
	}
	
	private TradeResponse createSampleTradeResponse(String tradeId, String userId) {
		return new TradeResponse(
				tradeId,
				userId,
				"BTC-EUR",
				"BUY",
				new BigDecimal("5"),
				new BigDecimal("65000.00"),
				"Crypto",
				"PENDING",
				Instant.now(),
				Instant.now()
				);
	}

}
