package dk.jnie.example.domain.repository;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

public interface CacheRepository {

    record CacheEntry(
        String cacheKey,
        Object data,
        Instant createdAt,
        Instant expiresAt
    ) {}

    CompletableFuture<Void> store(String cacheKey, Object data, long ttlSeconds);

    CompletableFuture<CacheEntry> retrieve(String cacheKey);

    CompletableFuture<Void> invalidate(String cacheKey);

    CompletableFuture<Integer> cleanup();
}