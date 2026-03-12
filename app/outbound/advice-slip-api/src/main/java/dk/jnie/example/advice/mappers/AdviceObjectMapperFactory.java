package dk.jnie.example.advice.mappers;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;

@Factory
public class AdviceObjectMapperFactory {

    @Bean
    @Singleton
    public AdviceObjectMapper createMapper() {
        return new AdviceObjectMapperImpl();
    }
}