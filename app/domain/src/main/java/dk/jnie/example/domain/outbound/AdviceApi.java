package dk.jnie.example.domain.outbound;

import dk.jnie.example.domain.model.MultiAggregate;

import java.util.concurrent.CompletableFuture;

/**
 * AdviceApi is an interface that defines a contract for fetching random advice.
 * It returns a {@link CompletableFuture} that contains the {@link MultiAggregate}.
 * This interface can be implemented with Micronaut, Spring, or any other HTTP client.
 */
public interface AdviceApi {

    /**
     * Fetches a random piece of advice.
     *
     * <p>This method is designed to be non-blocking and returns a {@link CompletableFuture},
     * which represents a single asynchronous computation. The CompletableFuture will complete
     * with a {@link MultiAggregate} object that encapsulates the random advice data.</p>
     *
     * @return a {@link CompletableFuture} that will complete with the {@link MultiAggregate} 
     *         object containing the advice data, or complete exceptionally if the fetch fails.
     */
    CompletableFuture<MultiAggregate> getRandomAdvice();
}