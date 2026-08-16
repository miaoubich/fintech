package com.miaoubich.model;

import java.time.Instant;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "outbox_events", indexes = {
		@Index(name = "idx_outbox_events_processed", columnList = "processed, createdAt"),
		@Index(name = "idx_outbox_events_aggregate", columnList = "aggregateId") })
public class OutboxEvent {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "outbox_event_seq_gen")
	@SequenceGenerator(name = "outbox_event_seq_gen", sequenceName = "outbox_events_seq", allocationSize = 50)
	private Long id;

	@Column(nullable = false, length = 100)
	private String eventType; // TRADE_EXECUTED

	@Column(nullable = false, length = 100)
	private String aggregateId; // tradeId

	@Column(nullable = false, length = 100)
	private String aggregateType;

	@Column(nullable = false, columnDefinition = "jsonb")
	@JdbcTypeCode(SqlTypes.JSON)
	private String payload; // JSON of TradeEvent

	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	@Column(nullable = false)
	private boolean processed;

	private Instant processedAt;

	@Version
	private Long version;

	public OutboxEvent() {
	}

	public OutboxEvent(String eventType, String aggregateId, String aggregateType, String payload) {
		super();
		this.eventType = eventType;
		this.aggregateId = aggregateId;
		this.aggregateType = aggregateType;
		this.payload = payload;
		this.createdAt = Instant.now();
		this.processed = false;
	}
	
	public void markAsProcessed() {
        this.processed = true;
        this.processedAt = Instant.now();
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

	public String getAggregateType() {
		return aggregateType;
	}

	public void setAggregateType(String aggregateType) {
		this.aggregateType = aggregateType;
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

	public Instant getProcessedAt() {
		return processedAt;
	}

	public void setProcessedAt(Instant processedAt) {
		this.processedAt = processedAt;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}
