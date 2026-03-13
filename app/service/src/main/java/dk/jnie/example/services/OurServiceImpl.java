package dk.jnie.example.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.jnie.example.domain.model.MultiAggregate;
import dk.jnie.example.domain.outbound.AdviceApi;
import dk.jnie.example.domain.repository.CacheRepository;
import dk.jnie.example.domain.services.OurService;
import dk.jnie.example.domain.model.DomainRequest;
import dk.jnie.example.domain.model.DomainResponse;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

@Singleton
public class OurServiceImpl implements OurService {

    private static final Logger LOG = LoggerFactory.getLogger(OurServiceImpl.class);
    private static final long CACHE_TTL_SECONDS = 300;

    private final AdviceApi adviceAPI;
    private final CacheRepository cacheRepository;
    private final ObjectMapper objectMapper;

    @Inject
    public OurServiceImpl(AdviceApi adviceAPI, CacheRepository cacheRepository, ObjectMapper objectMapper) {
        this.adviceAPI = adviceAPI;
        this.cacheRepository = cacheRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public CompletableFuture<DomainResponse> getAnAdvice(DomainRequest domainRequest) {
        String cacheKey = "advice:" + domainRequest.getQuestion();
        LOG.debug("Requesting advice for: {} (cache key: {})", domainRequest.getQuestion(), cacheKey);

        return cacheRepository.retrieve(cacheKey)
                .thenCompose(entry -> {
                    if (entry != null) {
                        LOG.info("Cache HIT for key: {}", cacheKey);
                        try {
                            MultiAggregate cachedAdvice = objectMapper.readValue(entry.responseData(), MultiAggregate.class);
                            return CompletableFuture.completedFuture(cachedAdvice);
                        } catch (JsonProcessingException e) {
                            LOG.warn("Failed to parse cached response, fetching fresh data", e);
                            return adviceAPI.getRandomAdvice()
                                    .thenCompose(advice -> cacheRepository.store(cacheKey, "", serialize(advice), CACHE_TTL_SECONDS)
                                            .thenApply(__ -> advice));
                        }
                    }

                    LOG.info("Cache MISS for key: {}, fetching from API", cacheKey);
                    return adviceAPI.getRandomAdvice()
                            .thenCompose(advice -> cacheRepository.store(cacheKey, "", serialize(advice), CACHE_TTL_SECONDS)
                                    .thenApply(__ -> advice));
                })
                .thenApply(advice -> DomainResponse.builder()
                        .answer(advice.getAnswer())
                        .build());
    }

    private String serialize(MultiAggregate advice) {
        try {
            return objectMapper.writeValueAsString(advice);
        } catch (JsonProcessingException e) {
            LOG.error("Failed to serialize advice", e);
            return "{}";
        }
    }
}