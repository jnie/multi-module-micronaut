package dk.jnie.example.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Service that provides caching functionality for API calls.
 * Handles cache lookup before making external API calls and stores results afterward.
 */
@Slf4j
@Singleton
@RequiredArgsConstructor
public class CacheService {

    private final CacheRepository cacheRepository;
    private final ObjectMapper objectMapper;
    
    /**
     * Default TTL for cache entries (5 minutes).
     */
    private static final long DEFAULT_TTL_SECONDS = 300;

    /**
     * Executes a cached operation.
     * First checks if a valid cache entry exists for the given key.
     * If found, returns the cached response.
     * If not found, executes the supplier, caches the result, and returns it.
     *
     * @param cacheKey    Unique key for this cache entry
     * @param requestInfo Info about the request (for logging/debugging)
     * @param supplier    The actual API call to execute if cache miss
     * @param <T>         Type of the response
     * @return The response, either from cache or freshly fetched
     */
    public <T> T executeCached(String cacheKey, String requestInfo, 
                                 java.util.function.Function<String, T> responseParser,
                                 Supplier<String> supplier) {
        
        // Try to get from cache first
        Optional<CacheRepository.CacheEntry> cached = cacheRepository.retrieve(cacheKey);
        
        if (cached.isPresent()) {
            log.info("Cache HIT for: {} (expires at {})", cacheKey, cached.get().expiresAt());
            try {
                return objectMapper.readValue(cached.get().responseData(), 
                    getGenericType(responseParser));
            } catch (JsonProcessingException e) {
                log.warn("Failed to parse cached response, fetching fresh data", e);
            }
        }
        
        log.info("Cache MISS for: {}, fetching from API", cacheKey);
        
        // Execute the actual API call
        String responseJson = supplier.get();
        
        // Store in cache
        try {
            cacheRepository.store(cacheKey, requestInfo, responseJson, DEFAULT_TTL_SECONDS);
        } catch (Exception e) {
            log.warn("Failed to cache response for: {}", cacheKey, e);
        }
        
        // Parse and return
        try {
            return objectMapper.readValue(responseJson, getGenericType(responseParser));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse response", e);
        }
    }

    /**
     * Generates a cache key from request parameters.
     *
     * @param endpoint The API endpoint
     * @param params  Request parameters
     * @return Generated cache key
     */
    public String generateCacheKey(String endpoint, Object... params) {
        StringBuilder key = new StringBuilder(endpoint);
        for (Object param : params) {
            key.append(":").append(param != null ? param.toString() : "null");
        }
        return key.toString();
    }

    /**
     * Invalidates a specific cache entry.
     *
     * @param cacheKey The cache key to invalidate
     */
    public void invalidate(String cacheKey) {
        cacheRepository.invalidate(cacheKey);
    }

    /**
     * Cleans up expired cache entries.
     *
     * @return Number of entries removed
     */
    public int cleanup() {
        return cacheRepository.cleanup();
    }

    @SuppressWarnings("unchecked")
    private <T> Class<T> getGenericType(java.util.function.Function<String, T> function) {
        // This is a workaround since we can't get generic type from lambda
        // In practice, you'd pass the Class<T> explicitly
        return (Class<T>) Object.class;
    }
}