package com.miaoubich.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.miaoubich.dto.TradeEvent;
import com.miaoubich.dto.TradeResponse;
import com.miaoubich.service.TradeService;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;

@Controller("/trades")
public class TradeController {

    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @Post
    public void createTrade(@Body TradeEvent event) throws IOException {
        tradeService.executeTrade(event);
    }
    
    @Get
    public List<TradeResponse> getTrades(@QueryValue Optional<String> userId) {
    	return userId
                .map(tradeService::getTradesByUserId)
                .orElseGet(tradeService::getAllTrades);
	}
    
    @Get("/health")
    public String healthCheck() {
		return "Trade service is up and running!";
	}
    
}