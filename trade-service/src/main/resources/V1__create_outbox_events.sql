CREATE SEQUENCE IF NOT EXISTS outbox_events_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS outbox_events (
    id BIGINT PRIMARY KEY DEFAULT nextval('outbox_events_seq'),

    event_type VARCHAR(100) NOT NULL,
    aggregate_id VARCHAR(100) NOT NULL,
    aggregate_type VARCHAR(100) NOT NULL,

    payload JSONB NOT NULL,

    created_at TIMESTAMPTZ NOT NULL,
    processed BOOLEAN NOT NULL DEFAULT FALSE,
    processed_at TIMESTAMPTZ,

    version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_outbox_events_processed
    ON outbox_events (processed, created_at);

CREATE INDEX IF NOT EXISTS idx_outbox_events_unprocessed
    ON outbox_events (created_at)
    WHERE processed = false;

CREATE INDEX IF NOT EXISTS idx_outbox_events_aggregate
    ON outbox_events (aggregate_id);