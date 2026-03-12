package dk.jnie.example.services;

import dk.jnie.example.domain.outbound.AdviceApi;
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

    private final AdviceApi adviceAPI;

    @Inject
    public OurServiceImpl(AdviceApi adviceAPI) {
        this.adviceAPI = adviceAPI;
    }

    @Override
    public CompletableFuture<DomainResponse> getAnAdvice(DomainRequest domainRequest) {
        LOG.debug("Requesting advice for: {}", domainRequest.getQuestion());
        
        return adviceAPI.getRandomAdvice()
                .thenApply(advice -> DomainResponse.builder()
                        .answer(advice.getAnswer())
                        .build());
    }
}