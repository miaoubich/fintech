package com.miaoubich.kafka;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.MessageHeader;

@KafkaClient(id = "trade-event-producer")
public interface TradeProducer {

	@Topic("${app.kafka.topics.trade-events:trade-events}")
    void send(@KafkaKey String aggregateId, 
    		     String event, 
    		     @MessageHeader String eventType, 
    		     @MessageHeader String aggregateType);
}
