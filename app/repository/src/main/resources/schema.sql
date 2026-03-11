-- H2 Schema for Cache Repository
-- This script creates the cache table for storing API responses

-- Create the cache_store table
CREATE TABLE IF NOT EXISTS cache_store (
    cache_key VARCHAR(512) PRIMARY KEY,
    request_params CLOB,
    response_data CLOB,
    created_at TIMESTAMP,
    last_accessed_at TIMESTAMP,
    expires_at TIMESTAMP,
    hit BOOLEAN DEFAULT FALSE,
    access_count INT DEFAULT 0
);

-- Create index on expires_at for efficient cleanup queries
CREATE INDEX IF NOT EXISTS idx_cache_expires ON cache_store(expires_at);

-- Create index on last_accessed_at for potential LRU cleanup
CREATE INDEX IF NOT EXISTS idx_cache_accessed ON cache_store(last_accessed_at);