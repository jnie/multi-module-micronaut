package dk.jnie.example.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Entity representing a cached API response.
 * Stores request parameters, response data, and timestamps for cache management.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheEntity {

    /**
     * Unique cache key (typically generated from request parameters).
     */
    private String cacheKey;

    /**
     * The original request parameters serialized as JSON.
     */
    private String requestParams;

    /**
     * The cached response data serialized as JSON.
     */
    private String responseData;

    /**
     * Timestamp when the cache entry was created.
     */
    private Instant createdAt;

    /**
     * Timestamp when the cache entry was last accessed.
     */
    private Instant lastAccessedAt;

    /**
     * Timestamp when the cache entry expires (TTL).
     */
    private Instant expiresAt;

    /**
     * Flag indicating if this was a cache hit (true) or miss (false).
     */
    private boolean hit;

    /**
     * Number of times this cache entry has been accessed.
     */
    private int accessCount;
}