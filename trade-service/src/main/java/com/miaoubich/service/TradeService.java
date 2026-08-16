package com.miaoubich.service;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miaoubich.dto.TradeEvent;
import com.miaoubich.dto.TradeResponse;
import com.miaoubich.model.OutboxEvent;
import com.miaoubich.model.Trade;
import com.miaoubich.repository.OutboxEventRepository;
import com.miaoubich.repository.TradeRepository;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.ObjectMapper;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

@Singleton
public class TradeService {

	private static final Logger LOG = LoggerFactory.getLogger(TradeService.class);
	
    private final OutboxEventRepository outboxEventRepository;
    private final TradeRepository tradeRepository;
    private final ObjectMapper jsonMapper;

    public TradeService(OutboxEventRepository outboxEventRepository, ObjectMapper jsonMapper,
    		TradeRepository tradeRepository) {
		this.outboxEventRepository = outboxEventRepository;
		this.tradeRepository = tradeRepository;
		this.jsonMapper = jsonMapper;
    }

    @Transactional
    public void executeTrade(TradeEvent tradeEvent) throws IOException {
    	TradeEvent tradeEventPending = new TradeEvent(
				tradeEvent.tradeId(),
				tradeEvent.userId(),
				tradeEvent.symbol(),
				tradeEvent.side(),
				tradeEvent.quantity(),
				tradeEvent.price(),
				tradeEvent.asset(),
				"PENDING",
				Instant.now()
		);
    	
    	Trade trade = new Trade(
				tradeEventPending.tradeId(),
				tradeEventPending.userId(),
				tradeEventPending.symbol(),
				tradeEventPending.side(),
				tradeEventPending.quantity(),
				tradeEventPending.price(),
				tradeEventPending.status(),
				tradeEventPending.timestamp()
		);
    	
    	String payload = serializePayload(tradeEventPending);
    	
    	// Create outbox event in the SAME transaction as your business logic
    	OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setEventType(TradeEvent.EVENT_TYPE_CREATED);
        outboxEvent.setAggregateId(tradeEvent.tradeId());
        outboxEvent.setAggregateType(TradeEvent.AGGREGATE_TYPE);
        outboxEvent.setPayload(payload);
        outboxEvent.setCreatedAt(Instant.now());

        outboxEventRepository.save(outboxEvent);
        tradeRepository.save(trade);
    }

    public List<TradeResponse> getAllTrades() {
        return tradeRepository.findAllTradesOrderByCreatedAtDesc()
                .stream()
                .map(TradeResponse::from)
                .toList();
    }
    
    public List<TradeResponse> getTradesByUserId(String userId) {
        return tradeRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(TradeResponse::from)
                .toList();
    }
    
	private @NonNull String serializePayload(TradeEvent tradeEventPending) {
		try {
			return new String(jsonMapper.writeValueAsString(tradeEventPending));
		} catch (IOException e) {
			LOG.error("Failed to serialize TradeEvent payload", e);
			throw new RuntimeException("Failed to serialize TradeEvent payload", e);
		}
	}

}
