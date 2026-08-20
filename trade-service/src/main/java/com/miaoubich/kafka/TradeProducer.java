package com.miaoubich.kafka;

import com.miaoubich.dto.TradeEvent;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient(id = "trade-event-producer")
public interface TradeProducer {

	@Topic("trades-events")//"${app.kafka.topics.trade:trade-events}")
    void send(@KafkaKey String aggregateId, 
    		     TradeEvent event);
}
