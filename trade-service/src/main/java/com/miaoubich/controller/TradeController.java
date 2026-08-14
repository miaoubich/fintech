package com.miaoubich.controller;

import java.io.IOException;

import com.miaoubich.dto.TradeEvent;
import com.miaoubich.service.TradeService;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

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
    
    @Get("/health")
    public String healthCheck() {
		return "Trade service is up and running!";
	}
    
}