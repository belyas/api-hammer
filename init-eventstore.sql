-- Event Store Schema
-- Append-only event log with optimistic locking

CREATE TABLE IF NOT EXISTS event_store (
    event_id UUID PRIMARY KEY,
    aggregate_id UUID NOT NULL,
    aggregate_type VARCHAR(100) NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    event_version INT NOT NULL,
    event_data JSONB NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    sequence_number BIGSERIAL,
    
    CONSTRAINT unique_aggregate_version 
        UNIQUE (aggregate_id, event_version)
);

-- Indexes for fast event retrieval
CREATE INDEX IF NOT EXISTS idx_event_store_aggregate 
    ON event_store(aggregate_id, event_version);

CREATE INDEX IF NOT EXISTS idx_event_store_type 
    ON event_store(aggregate_type, created_at);

CREATE INDEX IF NOT EXISTS idx_event_store_sequence 
    ON event_store(sequence_number);

-- Grant permissions
GRANT ALL PRIVILEGES ON TABLE event_store TO es_user;
GRANT USAGE, SELECT ON SEQUENCE event_store_sequence_number_seq TO es_user;
