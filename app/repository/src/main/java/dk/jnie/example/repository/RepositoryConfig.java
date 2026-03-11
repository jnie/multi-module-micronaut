package dk.jnie.example.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

/**
 * Configuration for the H2 cache database.
 * Sets up an in-memory H2 database with JDBC connectivity.
 */
@Slf4j
@Factory
public class RepositoryConfig {

    /**
     * Creates and configures the H2 in-memory DataSource.
     * Database is named 'testdb' and initialized with schema.sql.
     *
     * @return Configured H2 DataSource
     */
    @Bean(preDestroy = "close")
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL");
        config.setUsername("sa");
        config.setPassword("");
        config.setDriverClassName("org.h2.Driver");
        
        // Pool settings
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(10000);
        config.setIdleTimeout(300000);
        config.setMaxLifetime(600000);
        
        // Initialize schema
        config.setInitializationFailTimeout(0);
        
        log.info("Initializing H2 in-memory database for caching");
        
        return new HikariDataSource(config);
    }
}