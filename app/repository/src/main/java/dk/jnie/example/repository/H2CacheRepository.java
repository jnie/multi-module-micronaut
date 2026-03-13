package dk.jnie.example.repository;

import dk.jnie.example.domain.repository.CacheRepository;
import jakarta.inject.Singleton;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Singleton
public class H2CacheRepository implements CacheRepository {

    private final ConcurrentHashMap<String, CacheRepository.CacheEntry> cache = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    @Override
    public CompletableFuture<CacheRepository.CacheEntry> retrieve(String cacheKey) {
        return CompletableFuture.supplyAsync(() -> {
            CacheRepository.CacheEntry entry = cache.get(cacheKey);
            if (entry != null && entry.expiresAt().isAfter(Instant.now())) {
                return entry;
            }
            if (entry != null) {
                cache.remove(cacheKey);
            }
            return (CacheRepository.CacheEntry) null;
        }, executor);
    }

    @Override
    public CompletableFuture<Void> store(String cacheKey, Object data, long ttlSeconds) {
        return CompletableFuture.runAsync(() -> {
            Instant now = Instant.now();
            CacheRepository.CacheEntry entry = new CacheRepository.CacheEntry(cacheKey, data, now, now.plusSeconds(ttlSeconds));
            cache.put(cacheKey, entry);
        }, executor);
    }

    @Override
    public CompletableFuture<Void> invalidate(String cacheKey) {
        return CompletableFuture.runAsync(() -> cache.remove(cacheKey), executor);
    }

    @Override
    public CompletableFuture<Integer> cleanup() {
        return CompletableFuture.supplyAsync(() -> {
            Instant now = Instant.now();
            int removed = 0;
            for (String key : cache.keySet()) {
                CacheRepository.CacheEntry entry = cache.get(key);
                if (entry != null && entry.expiresAt().isBefore(now)) {
                    cache.remove(key);
                    removed++;
                }
            }
            return removed;
        }, executor);
    }
}