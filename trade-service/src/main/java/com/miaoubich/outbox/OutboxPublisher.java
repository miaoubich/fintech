package com.miaoubich.outbox;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miaoubich.kafka.TradeProducer;
import com.miaoubich.model.OutboxEvent;
import com.miaoubich.repository.OutboxEventRepository;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

@Singleton
public class OutboxPublisher {

    private static final Logger LOG = LoggerFactory.getLogger(OutboxPublisher.class);
    private static final int BATCH_SIZE = 10;

    private final OutboxEventRepository repository;
    private final TradeProducer tradeProducer;

    public OutboxPublisher(OutboxEventRepository repository,
    					   TradeProducer tradeProducer) {
        this.repository = repository;
        this.tradeProducer = tradeProducer;
    }

    @Scheduled(fixedDelay = "${app.outbox.poll-interval:5s}",
    		   initialDelay = "${app.outbox.initial-delay:10s}")
    @Transactional
    public void publishPendingEvents() {

    	// unprocessed events
        List<OutboxEvent> pendingEvents = repository.findUnprocessedEventsForUpdate();

        if (pendingEvents.isEmpty()) {
            return;
        }

        LOG.info("Found {} pending outbox events", pendingEvents.size());

        for (OutboxEvent event : pendingEvents) {
            try {
                tradeProducer.send(event.getAggregateId(),
                		           event.getPayload(),
                		           event.getEventType(),
                		           event.getAggregateType());
                
                event.markAsProcessed();
                repository.update(event);

                LOG.info("Published OutboxEvent id={}, type={}, aggregateId={} successfully", 
                		event.getId(), event.getEventType(), event.getAggregateId());

            } catch (Exception e) {
                LOG.error("Failed to publish OutboxEvent id={}, error={} -> Will retry.", 
                		event.getId(), e.getMessage(), e);
                // If Kafka fails, the processed remains false → scheduler retries next cycle
                break;
            }
        }
    }
}
