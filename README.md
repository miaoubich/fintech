# System Architecture Overview
This project currently consists of two fully formed microservices, implementing a clean CQRS + Event‑Driven Architecture similar to Bitpanda, Revolut, and other 
modern trading platforms.

## 1. Trade‑Service — Command Side (Write Model)
The Trade‑Service is responsible for handling commands, executing trades, and publishing domain events using the Outbox Pattern.

## Representation Layer (API Layer)
TradeController handles:
* HTTP commands
* trade creation
* trade execution
* returning responses

## Functional Layer (Domain/Application Layer)
TradeService implements:
* business rules
* pending → executed transitions
* outbox event creation
* reliable event publishing

## Persistence Layer (Data Layer)
TradeRepository & OutboxEventRepository + JPA entities handle:
* storing trades
* storing outbox events

## Integration Layer (Messaging Layer)
TradeProducer is a Kafka Producer that handles:
* reliable event publication
* decoupling write-side from read-side
### This service represents the CQRS command‑side microservice.

# 2. Ledger‑Service — Read Side (Query Model)
The Ledger‑Service consumes trade events, applies idempotent ledger updates, maintains balances, and exposes read‑only APIs.

## Representation Layer (API Layer)
LedgerConctroller handles:
* balance queries
* portfolio queries
* ledger entry queries
* HTTP responses

## Functional Layer (Domain/Application Layer)
EventProcessService (write-side via Kafka)
LedgerService (read-side via REST)
Implements:
* idempotency
* ledger entry creation
* balance updates
* DTO mapping
* domain logic

## Persistence Layer (Data Layer)
AccountBalanceRepository, LedgerEntryRepository, and ProcessedTradeRepository + JPA entities handle:
* storing ledger entries
* storing account balances
* storing processed trades

## Integration Layer (Messaging Layer)
TradeEventConsumer, a Kafka Consumer that handles:
* event ingestion
* retry semantics
* offset management
### This service represents the CQRS read‑side microservice.

# Summary
Together, these two microservices form a complete event‑driven trading pipeline:
* Trade‑Service → publishes TRADE_EXECUTED events
* Ledger‑Service → consumes events and updates the financial ledger

This architecture ensures:
* strong separation of concerns
* high scalability
* auditability
* correctness
* fault tolerance
* clean domain boundaries
