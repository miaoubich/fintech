package com.miaoubich.dto;

import java.math.BigDecimal;
import java.time.Instant;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record TradeEvent(

		String tradeId, 
		String userId, 
		String symbol, 
		String side, // BYU or SELL
		BigDecimal quantity, 
		BigDecimal price, 
		String asset, // AAPL, TSLA, MSFT, BTC-EUR, ETH-EUR, EUR/USD, USD/JPY
		String status, // PENDING, EXECUTED, CANCELLED
		Instant timestamp) 
{
	public static final String AGGREGATE_TYPE = "Trade";
	public static final String EVENT_TYPE_CREATED = "TRADE_CREATED";
	public static final String EVENT_TYPE_EXECUTED = "TRADE_EXECUTED";
	public static final String EVENT_TYPE_CANCELLED = "TRADE_CANCELLED";
}
