package dk.jnie.example.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.jnie.example.advice.mappers.AdviceObjectMapper;
import dk.jnie.example.advice.model.AdviceResponse;
import dk.jnie.example.domain.model.MultiAggregate;
import dk.jnie.example.domain.outbound.AdviceApi;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@Singleton
public class AdviceApiImpl implements AdviceApi {

    private static final Logger LOG = LoggerFactory.getLogger(AdviceApiImpl.class);

    private final ObjectMapper objectMapper;
    private final AdviceObjectMapper mapper;
    private final String apiUrl;

    @Inject
    public AdviceApiImpl(ObjectMapper objectMapper, AdviceObjectMapper mapper,
                         io.micronaut.context.annotation.Value("${mma.outbound.advice-slip-api.url}") String apiUrl) {
        this.objectMapper = objectMapper;
        this.mapper = mapper;
        this.apiUrl = apiUrl;
    }

    @Override
    public CompletableFuture<MultiAggregate> getRandomAdvice() {
        LOG.info("Calling the advice API");

        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(apiUrl + "/advice"))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String responseBody = response.body();

                AdviceResponse adviceResponse = objectMapper.readValue(responseBody, AdviceResponse.class);

                return mapper.toDomain(adviceResponse).orElseThrow(() -> 
                    new RuntimeException("Failed to map advice response"));

            } catch (Exception e) {
                LOG.error("Failed to call advice API", e);
                throw new RuntimeException("Failed to call advice API", e);
            }
        });
    }
}