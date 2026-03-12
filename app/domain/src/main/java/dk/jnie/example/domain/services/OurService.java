package dk.jnie.example.domain.services;

import dk.jnie.example.domain.model.DomainRequest;
import dk.jnie.example.domain.model.DomainResponse;

import java.util.concurrent.CompletableFuture;

public interface OurService {

    CompletableFuture<DomainResponse> getAnAdvice(DomainRequest domainRequest);
}