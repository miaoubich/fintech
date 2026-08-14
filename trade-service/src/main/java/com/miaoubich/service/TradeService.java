package com.miaoubich.service;

import java.io.IOException;
import java.time.Instant;

import com.miaoubich.dto.TradeEvent;
import com.miaoubich.kafka.TradeProducer;
import com.miaoubich.model.OutboxEvent;
import com.miaoubich.repository.OutboxEventRepository;
import io.micronaut.serde.ObjectMapper;


import jakarta.inject.Singleton;

@Singleton
public class TradeService {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper jsonMapper;

    public TradeService(OutboxEventRepository outboxEventRepository, ObjectMapper jsonMapper) {
		this.outboxEventRepository = outboxEventRepository;
		this.jsonMapper = jsonMapper;
    }

    public void executeTrade(TradeEvent tradeEvent) throws IOException {
    	OutboxEvent event = new OutboxEvent();
        event.setEventType("TRADE_EXECUTED");
        event.setAggregateId(tradeEvent.getTradeId());
        event.setPayload(jsonMapper.writeValueAsString(tradeEvent));
        event.setCreatedAt(Instant.now());

        outboxEventRepository.save(event);
    }
}
