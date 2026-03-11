package dk.jnie.example.controllers;

import dk.jnie.example.rest.model.RequestDto;
import dk.jnie.example.rest.model.ResponseDto;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * Unit tests for MainController using Micronaut test framework.
 */
@MicronautTest
@DisplayName("MainController Tests")
class MainControllerTest {

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
        // Use mock service via the endpoint - returns 200 since service is properly wired
        String jsonBody = "{\"please\":\"anything\"}";
        HttpRequest<String> request = HttpRequest.POST("/api/v1/advice", jsonBody)
                .contentType(MediaType.APPLICATION_JSON_TYPE);

        HttpResponse<ResponseDto> response = blockingClient.exchange(request, ResponseDto.class);

        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isPresent();
    }

    @Test
    @DisplayName("POST /api/v1/advice handles empty request")
    void getAdvice_HandlesEmptyRequest() {
        // When sending an empty object, should get validation error or handle gracefully
        String jsonBody = "{}";
        HttpRequest<String> request = HttpRequest.POST("/api/v1/advice", jsonBody)
                .contentType(MediaType.APPLICATION_JSON_TYPE);

        try {
            HttpResponse<ResponseDto> response = blockingClient.exchange(request, ResponseDto.class);
            // If it succeeds, that's fine too
            assertThat(response.getStatus()).isIn(HttpStatus.OK, HttpStatus.BAD_REQUEST);
        } catch (HttpClientResponseException e) {
            // Expected - empty request might fail validation
            assertThat(e.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }
}