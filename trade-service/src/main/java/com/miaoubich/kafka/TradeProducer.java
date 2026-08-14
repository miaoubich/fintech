package com.miaoubich.kafka;

import com.miaoubich.dto.TradeEvent;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface TradeProducer {

	@Topic("${bitpanda.topics.trades}")
    void sendTrade(TradeEvent event);
}
