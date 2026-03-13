package dk.jnie.example.repository;

import dk.jnie.example.domain.repository.CacheRepository;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Factory
public class RepositoryConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        log.info("Initializing H2 R2DBC connection factory for caching");

        H2ConnectionConfiguration config = H2ConnectionConfiguration.builder()
                .inMemory("testdb")
                .username("sa")
                .password("")
                .build();

        return new H2ConnectionFactory(config);
    }

    @Bean
    @Singleton
    public CacheRepository cacheRepository(ConnectionFactory connectionFactory) {
        return new H2CacheRepository(connectionFactory);
    }
}