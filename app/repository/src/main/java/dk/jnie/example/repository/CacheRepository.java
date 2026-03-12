package dk.jnie.example.repository;

import java.util.Optional;

/**
 * Repository interface for cache operations.
 * Provides methods to store, retrieve, and manage cached API responses.
 */
public interface CacheRepository {

    /**
     * Stores a response in the cache with the given key.
     *
     * @param cacheKey      The unique key for this cache entry
     * @param requestParams The request parameters as JSON string
     * @param responseData  The response data as JSON string
     * @param ttlSeconds   Time-to-live in seconds for this cache entry
     */
    void store(String cacheKey, String requestParams, String responseData, long ttlSeconds);

    /**
     * Retrieves a cached response by key if it exists and hasn't expired.
     *
     * @param cacheKey The cache key to look up
     * @return Optional containing the cached response data if found and valid, empty otherwise
     */
    Optional<CacheEntry> retrieve(String cacheKey);

    /**
     * Checks if a cache entry exists and is valid (not expired).
     *
     * @param cacheKey The cache key to check
     * @return true if a valid cache entry exists, false otherwise
     */
    boolean isValid(String cacheKey);

    /**
     * Invalidates (deletes) a cache entry.
     *
     * @param cacheKey The cache key to invalidate
     */
    void invalidate(String cacheKey);

    /**
     * Removes all expired cache entries.
     *
     * @return Number of entries removed
     */
    int cleanup();

    /**
     * Simple record to hold cache entry data.
     */
    record CacheEntry(String cacheKey, String requestParams, String responseData, 
                      java.time.Instant createdAt, java.time.Instant expiresAt) {}
}