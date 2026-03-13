package dk.jnie.example.repository;

import dk.jnie.example.domain.repository.CacheRepository;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class H2CacheRepository implements CacheRepository {

    private final ConnectionFactory connectionFactory;
    private volatile boolean schemaInitialized = false;

    public H2CacheRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    private void ensureSchema() {
        if (!schemaInitialized) {
            synchronized (this) {
                if (!schemaInitialized) {
                    initializeSchema().block();
                    schemaInitialized = true;
                }
            }
        }
    }

    @Override
    public CompletableFuture<Void> store(String cacheKey, String requestParams, String responseData, long ttlSeconds) {
        ensureSchema();
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(ttlSeconds);

        String sql = "INSERT INTO cache_store (cache_key, request_params, response_data, created_at, last_accessed_at, expires_at, hit, access_count) " +
                     "VALUES ($1, $2, $3, $4, $5, $6, $7, $8)";

        return Mono.from(connectionFactory.create())
                .flatMap(conn -> Mono.from(conn.createStatement(sql)
                        .bind(0, cacheKey)
                        .bind(1, requestParams)
                        .bind(2, responseData)
                        .bind(3, now)
                        .bind(4, now)
                        .bind(5, expiresAt)
                        .bind(6, false)
                        .bind(7, 0)
                        .execute()))
                .doOnSuccess(x -> log.debug("Cached response for key: {}", cacheKey))
                .doOnError(e -> log.error("Failed to store cache entry for key: {}", cacheKey, e))
                .then()
                .toFuture()
                .exceptionally(e -> {
                    throw new RuntimeException("Failed to store cache entry", e);
                });
    }

    @Override
    public CompletableFuture<CacheEntry> retrieve(String cacheKey) {
        String sql = "SELECT cache_key, request_params, response_data, created_at, expires_at FROM cache_store " +
                     "WHERE cache_key = $1 AND expires_at > $2";

        return Mono.from(connectionFactory.create())
                .flatMap(conn -> Mono.from(conn.createStatement(sql)
                        .bind(0, cacheKey)
                        .bind(1, Instant.now())
                        .execute()))
                .flatMapMany(result -> Flux.from(result.map((row, metadata) -> new CacheEntry(
                        row.get("cache_key", String.class),
                        row.get("request_params", String.class),
                        row.get("response_data", String.class),
                        row.get("created_at", Instant.class),
                        row.get("expires_at", Instant.class)
                ))))
                .singleOrEmpty()
                .doOnSuccess(entry -> {
                    if (entry != null) {
                        log.debug("Cache HIT for key: {}", cacheKey);
                    } else {
                        log.debug("Cache MISS for key: {}", cacheKey);
                    }
                })
                .doOnError(e -> log.error("Failed to retrieve cache entry for key: {}", cacheKey, e))
                .toFuture()
                .exceptionally(e -> {
                    log.error("Failed to retrieve cache entry for key: {}", cacheKey, e);
                    return null;
                });
    }

    @Override
    public CompletableFuture<Void> invalidate(String cacheKey) {
        String sql = "DELETE FROM cache_store WHERE cache_key = $1";

        return Mono.from(connectionFactory.create())
                .flatMap(conn -> Mono.from(conn.createStatement(sql)
                        .bind(0, cacheKey)
                        .execute()))
                .doOnSuccess(x -> log.debug("Invalidated cache entry for key: {}", cacheKey))
                .doOnError(e -> log.error("Failed to invalidate cache entry for key: {}", cacheKey, e))
                .then()
                .toFuture()
                .exceptionally(e -> {
                    throw new RuntimeException("Failed to invalidate cache entry", e);
                });
    }

    @Override
    public CompletableFuture<Integer> cleanup() {
        String sql = "DELETE FROM cache_store WHERE expires_at <= $1";

        return Mono.from(connectionFactory.create())
                .flatMap(conn -> Mono.from(conn.createStatement(sql)
                        .bind(0, Instant.now())
                        .execute()))
                .flatMap(result -> Mono.from(result.getRowsUpdated()))
                .map(Long::intValue)
                .defaultIfEmpty(0)
                .doOnSuccess(count -> {
                    if (count > 0) {
                        log.info("Cleaned up {} expired cache entries", count);
                    }
                })
                .doOnError(e -> log.error("Failed to cleanup expired cache entries", e))
                .toFuture()
                .exceptionally(e -> {
                    log.error("Failed to cleanup expired cache entries", e);
                    return 0;
                });
    }

    public Mono<Void> initializeSchema() {
        String createTableSql = """
            CREATE TABLE IF NOT EXISTS cache_store (
                cache_key VARCHAR(512) PRIMARY KEY,
                request_params CLOB,
                response_data CLOB,
                created_at TIMESTAMP,
                last_accessed_at TIMESTAMP,
                expires_at TIMESTAMP,
                hit BOOLEAN DEFAULT FALSE,
                access_count INT DEFAULT 0
            )
            """;
        
        String createIndex1 = "CREATE INDEX IF NOT EXISTS idx_cache_expires ON cache_store(expires_at)";
        String createIndex2 = "CREATE INDEX IF NOT EXISTS idx_cache_accessed ON cache_store(last_accessed_at)";

        return Mono.from(connectionFactory.create())
                .flatMap(conn -> Mono.from(conn.createStatement(createTableSql).execute())
                        .then(Mono.from(conn.createStatement(createIndex1).execute()))
                        .then(Mono.from(conn.createStatement(createIndex2).execute())))
                .doOnSuccess(x -> log.info("Cache schema initialized"))
                .doOnError(e -> log.warn("Schema init warning (table may already exist): {}", e.getMessage()))
                .then()
                .onErrorResume(e -> Mono.empty());
    }
}