package dk.jnie.example.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Optional;

/**
 * Implementation of CacheRepository using H2 in-memory database.
 * Provides JDBC-based caching with TTL support.
 */
@Slf4j
@RequiredArgsConstructor
public class CacheRepositoryImpl implements CacheRepository {

    private final DataSource dataSource;
    
    private static final String INSERT_SQL = 
        "INSERT INTO cache_store (cache_key, request_params, response_data, created_at, last_accessed_at, expires_at, hit, access_count) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SELECT_SQL = 
        "SELECT cache_key, request_params, response_data, created_at, expires_at FROM cache_store " +
        "WHERE cache_key = ? AND expires_at > ?";
    
    private static final String UPDATE_ACCESS_SQL = 
        "UPDATE cache_store SET last_accessed_at = ?, hit = true, access_count = access_count + 1 WHERE cache_key = ?";
    
    private static final String DELETE_SQL = "DELETE FROM cache_store WHERE cache_key = ?";
    
    private static final String CLEANUP_SQL = "DELETE FROM cache_store WHERE expires_at <= ?";

    @Override
    public void store(String cacheKey, String requestParams, String responseData, long ttlSeconds) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(ttlSeconds);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {
            
            stmt.setString(1, cacheKey);
            stmt.setString(2, requestParams);
            stmt.setString(3, responseData);
            stmt.setObject(4, now);
            stmt.setObject(5, now);
            stmt.setObject(6, expiresAt);
            stmt.setBoolean(7, false); // Not a hit on initial store
            stmt.setInt(8, 0); // Initial access count
            
            stmt.executeUpdate();
            log.debug("Cached response for key: {}", cacheKey);
            
        } catch (SQLException e) {
            log.error("Failed to store cache entry for key: {}", cacheKey, e);
            throw new RuntimeException("Failed to store cache entry", e);
        }
    }

    @Override
    public Optional<CacheEntry> retrieve(String cacheKey) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_SQL)) {
            
            stmt.setString(1, cacheKey);
            stmt.setObject(2, Instant.now());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Update access statistics
                    updateAccessStats(cacheKey);
                    
                    CacheEntry entry = new CacheEntry(
                        rs.getString("cache_key"),
                        rs.getString("request_params"),
                        rs.getString("response_data"),
                        rs.getObject("created_at", Instant.class),
                        rs.getObject("expires_at", Instant.class)
                    );
                    
                    log.debug("Cache HIT for key: {}", cacheKey);
                    return Optional.of(entry);
                }
            }
            
            log.debug("Cache MISS for key: {}", cacheKey);
            return Optional.empty();
            
        } catch (SQLException e) {
            log.error("Failed to retrieve cache entry for key: {}", cacheKey, e);
            return Optional.empty();
        }
    }

    private void updateAccessStats(String cacheKey) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_ACCESS_SQL)) {
            
            stmt.setObject(1, Instant.now());
            stmt.setString(2, cacheKey);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            log.warn("Failed to update access stats for key: {}", cacheKey, e);
        }
    }

    @Override
    public boolean isValid(String cacheKey) {
        return retrieve(cacheKey).isPresent();
    }

    @Override
    public void invalidate(String cacheKey) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            
            stmt.setString(1, cacheKey);
            int deleted = stmt.executeUpdate();
            
            if (deleted > 0) {
                log.debug("Invalidated cache entry for key: {}", cacheKey);
            }
            
        } catch (SQLException e) {
            log.error("Failed to invalidate cache entry for key: {}", cacheKey, e);
        }
    }

    @Override
    public int cleanup() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CLEANUP_SQL)) {
            
            stmt.setObject(1, Instant.now());
            int deleted = stmt.executeUpdate();
            
            if (deleted > 0) {
                log.info("Cleaned up {} expired cache entries", deleted);
            }
            
            return deleted;
            
        } catch (SQLException e) {
            log.error("Failed to cleanup expired cache entries", e);
            return 0;
        }
    }
}