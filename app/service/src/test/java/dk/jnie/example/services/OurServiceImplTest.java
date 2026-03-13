package dk.jnie.example.services;

import dk.jnie.example.domain.model.DomainRequest;
import dk.jnie.example.domain.model.DomainResponse;
import dk.jnie.example.domain.model.MultiAggregate;
import dk.jnie.example.domain.outbound.AdviceApi;
import dk.jnie.example.domain.repository.CacheRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
@DisplayName("OurServiceImpl Tests")
class OurServiceImplTest {

    @Mock
    private AdviceApi adviceAPI;

    @Mock
    private CacheRepository cacheRepository;

    private OurServiceImpl ourService;

    @BeforeEach
    void setUp() {
        ourService = new OurServiceImpl(adviceAPI, cacheRepository);
        
        when(cacheRepository.retrieve(anyString())).thenReturn(CompletableFuture.completedFuture(null));
        when(cacheRepository.store(anyString(), any(Object.class), anyLong()))
                .thenReturn(CompletableFuture.completedFuture(null));
    }

    @Test
    @DisplayName("getAnAdvice returns advice successfully")
    void getAnAdvice_ReturnsAdviceSuccessfully() throws ExecutionException, InterruptedException {
        String expectedAdvice = "Don't be afraid to ask questions.";
        DomainRequest request = DomainRequest.builder()
                .question("anything")
                .build();
        MultiAggregate mockAggregate = MultiAggregate.builder()
                .answer(expectedAdvice)
                .build();

        when(adviceAPI.getRandomAdvice()).thenReturn(CompletableFuture.completedFuture(mockAggregate));

        CompletableFuture<DomainResponse> result = ourService.getAnAdvice(request);

        assertThat(result.get().getAnswer()).isEqualTo(expectedAdvice);
        verify(adviceAPI).getRandomAdvice();
    }

    @Test
    @DisplayName("getAnAdvice handles empty advice response")
    void getAnAdvice_HandlesEmptyAdviceResponse() throws ExecutionException, InterruptedException {
        DomainRequest request = DomainRequest.builder()
                .question("test")
                .build();
        MultiAggregate mockAggregate = MultiAggregate.builder()
                .answer("")
                .build();

        when(adviceAPI.getRandomAdvice()).thenReturn(CompletableFuture.completedFuture(mockAggregate));

        CompletableFuture<DomainResponse> result = ourService.getAnAdvice(request);

        assertThat(result.get().getAnswer()).isEmpty();
    }

    @Test
    @DisplayName("getAnAdvice propagates error from AdviceApi")
    void getAnAdvice_PropagatesErrorFromAdviceApi() {
        DomainRequest request = DomainRequest.builder()
                .question("test")
                .build();
        RuntimeException expectedError = new RuntimeException("External API failed");

        when(adviceAPI.getRandomAdvice()).thenReturn(CompletableFuture.failedFuture(expectedError));

        try {
            ourService.getAnAdvice(request).get();
            throw new AssertionError("Expected ExecutionException");
        } catch (ExecutionException | InterruptedException e) {
            assertThat(e.getCause()).isInstanceOf(RuntimeException.class);
        }
    }

    @Test
    @DisplayName("getAnAdvice does not use domain request parameters")
    void getAnAdvice_DoesNotUseDomainRequestParameters() throws ExecutionException, InterruptedException {
        DomainRequest request = DomainRequest.builder()
                .question("this parameter is not used")
                .build();
        MultiAggregate mockAggregate = MultiAggregate.builder()
                .answer("API provides random advice")
                .build();

        when(adviceAPI.getRandomAdvice()).thenReturn(CompletableFuture.completedFuture(mockAggregate));

        CompletableFuture<DomainResponse> result = ourService.getAnAdvice(request);

        assertThat(result.get().getAnswer()).isEqualTo("API provides random advice");
    }
}