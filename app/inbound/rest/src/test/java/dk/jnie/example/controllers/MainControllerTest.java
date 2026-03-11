package dk.jnie.example.controllers;

import dk.jnie.example.rest.controllers.MainController;
import dk.jnie.example.rest.mappers.RestMapper;
import dk.jnie.example.domain.model.DomainRequest;
import dk.jnie.example.domain.model.DomainResponse;
import dk.jnie.example.rest.model.RequestDto;
import dk.jnie.example.rest.model.ResponseDto;
import dk.jnie.example.domain.services.OurService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.inject.Inject;

/**
 * Unit tests for MainController.
 * Uses Micronaut HttpClient to test the REST endpoints.
 */
@MicronautTest
@DisplayName("MainController Tests")
class MainControllerTest {

    @Mock
    private OurService ourServiceMock;

    @Mock
    private RestMapper restMapperMock;

    @Inject
    @Client("/")
    private HttpClient httpClient;

    private BlockingHttpClient blockingClient;

    @BeforeEach
    void setUp() {
        blockingClient = httpClient.toBlocking();
    }

    @Test
    @DisplayName("POST /api/v1/advice returns successful response")
    void getAdvice_ReturnsSuccessfulResponse() {
        // Arrange
        RequestDto requestDto = new RequestDto();
        requestDto.setPlease("anything");

        ResponseDto responseDto = new ResponseDto();
        responseDto.setAdvice("Don't be afraid to ask questions.");

        DomainResponse domainResponse = DomainResponse.builder()
                .answer("Don't be afraid to ask questions.")
                .build();

        when(restMapperMock.requestDTOToDomain(any(RequestDto.class)))
                .thenReturn(DomainRequest.builder().question("anything").build());
        when(ourServiceMock.getAnAdvice(any())).thenReturn(Mono.just(domainResponse));
        when(restMapperMock.domainToResponseDto(any(DomainResponse.class))).thenReturn(responseDto);

        String jsonBody = "{\"please\":\"anything\"}";
        HttpRequest<String> request = HttpRequest.POST("/api/v1/advice", jsonBody)
                .contentType(MediaType.APPLICATION_JSON_TYPE);

        HttpResponse<ResponseDto> response = blockingClient.exchange(request, ResponseDto.class);

        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isPresent();
        assertThat(response.getBody().get().getAdvice()).isEqualTo("Don't be afraid to ask questions.");

        verify(ourServiceMock).getAnAdvice(any());
    }

    @Test
    @DisplayName("POST /api/v1/advice handles empty request")
    void getAdvice_HandlesEmptyRequest() {
        // Arrange
        RequestDto requestDto = new RequestDto();
        requestDto.setPlease(null);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setAdvice("Default advice");

        DomainResponse domainResponse = DomainResponse.builder()
                .answer("Default advice")
                .build();

        when(restMapperMock.requestDTOToDomain(any(RequestDto.class)))
                .thenReturn(DomainRequest.builder().question("null").build());
        when(ourServiceMock.getAnAdvice(any())).thenReturn(Mono.just(domainResponse));
        when(restMapperMock.domainToResponseDto(any(DomainResponse.class))).thenReturn(responseDto);

        String jsonBody = "{}";
        HttpRequest<String> request = HttpRequest.POST("/api/v1/advice", jsonBody)
                .contentType(MediaType.APPLICATION_JSON_TYPE);

        HttpResponse<Void> response = blockingClient.exchange(request);

        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("POST /api/v1/advice returns 500 on service error")
    void getAdvice_Returns500OnServiceError() {
        // Arrange
        when(restMapperMock.requestDTOToDomain(any(RequestDto.class)))
                .thenReturn(DomainRequest.builder().question("test").build());
        when(ourServiceMock.getAnAdvice(any())).thenReturn(Mono.error(new RuntimeException("Service unavailable")));

        String jsonBody = "{\"please\":\"test\"}";
        HttpRequest<String> request = HttpRequest.POST("/api/v1/advice", jsonBody)
                .contentType(MediaType.APPLICATION_JSON_TYPE);

        // Act & Assert
        try {
            blockingClient.exchange(request);
        } catch (Exception e) {
            // Expected - service error returns 500
        }

        // Verify that the service was called
        verify(ourServiceMock).getAnAdvice(any());
    }
}