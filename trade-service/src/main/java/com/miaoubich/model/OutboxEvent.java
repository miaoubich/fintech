package com.miaoubich.model;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "outbox_events")
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String eventType; // TRADE_EXECUTED
    private String aggregateId; // tradeId
    @Lob
    private String payload; // JSON of TradeEvent
    private Instant createdAt;
    private boolean processed;
    
    public OutboxEvent() {}
	public OutboxEvent(String eventType, String aggregateId, String payload) {
		super();
		this.eventType = eventType;
		this.aggregateId = aggregateId;
		this.payload = payload;
		this.createdAt = Instant.now();
		this.processed = false;
	}
	
	public Long getId() {
		return id;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getAggregateId() {
		return aggregateId;
	}
	public void setAggregateId(String aggregateId) {
		this.aggregateId = aggregateId;
	}
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	public Instant getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	public boolean isProcessed() {
		return processed;
	}
	public void setProcessed(boolean processed) {
		this.processed = processed;
	}
    
}

