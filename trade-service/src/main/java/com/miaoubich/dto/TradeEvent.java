package com.miaoubich.dto;

import java.math.BigDecimal;
import java.time.Instant;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class TradeEvent {

	private String tradeId;
    private String userId;
    private BigDecimal amount;
    private String asset;
    private Instant timestamp;
    
	public String getTradeId() {
		return tradeId;
	}
	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getAsset() {
		return asset;
	}
	public void setAsset(String asset) {
		this.asset = asset;
	}
	public Instant getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}
	
    
}
