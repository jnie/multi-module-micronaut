package dk.jnie.example.rest.mappers;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;

@Factory
public class RestMapperFactory {

    @Bean
    @Singleton
    public RestMapper createMapper() {
        return new RestMapperImpl();
    }
}