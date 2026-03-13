package dk.jnie.example.repository;

import dk.jnie.example.domain.repository.CacheRepository;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Factory
public class RepositoryConfig {

    @Bean
    @Singleton
    public CacheRepository cacheRepository() {
        return new H2CacheRepository();
    }
}