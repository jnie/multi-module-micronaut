package dk.jnie.example.advice;

import dk.jnie.example.advice.mappers.AdviceObjectMapper;
import dk.jnie.example.advice.model.AdviceResponse;
import dk.jnie.example.domain.model.MultiAggregate;
import dk.jnie.example.domain.outbound.AdviceApi;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

@Singleton
public class AdviceApiImpl implements AdviceApi {

    private static final Logger LOG = LoggerFactory.getLogger(AdviceApiImpl.class);

    private final HttpClient httpClient;
    private final AdviceObjectMapper mapper;

    @Inject
    public AdviceApiImpl(@Client("${mma.outbound.advice-slip-api.url}") HttpClient httpClient,
                         AdviceObjectMapper mapper) {
        this.httpClient = httpClient;
        this.mapper = mapper;
    }

    @Override
    public CompletableFuture<MultiAggregate> getRandomAdvice() {
        LOG.info("Calling the advice API");

        return CompletableFuture.supplyAsync(() -> {
            try {
                AdviceResponse adviceResponse = httpClient.toBlocking()
                        .retrieve(HttpRequest.GET("/advice"), AdviceResponse.class);

                return mapper.toDomain(adviceResponse);

            } catch (Exception e) {
                LOG.error("Failed to call advice API", e);
                throw new RuntimeException("Failed to call advice API", e);
            }
        });
    }
}