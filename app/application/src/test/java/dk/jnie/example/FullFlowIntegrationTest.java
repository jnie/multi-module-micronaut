package dk.jnie.example;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import dk.jnie.example.rest.model.ResponseDto;
import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest(environments = "test")
@Property(name = "mma.outbound.advice-slip-api.url", value = "http://localhost:8765")
@DisplayName("Full Flow Integration Test")
class FullFlowIntegrationTest {

    @Inject
    @Client("/")
    HttpClient client;

    WireMockServer wireMockServer;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(wireMockConfig().port(8765));
        wireMockServer.start();
        WireMock.configureFor("localhost", 8765);
    }

    @AfterEach
    void tearDown() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }

    @Test
    @DisplayName("POST to /api/v1/advice should return advice from mocked external API")
    void testFullApiFlow() throws Exception {
        String adviceText = "Test advice from WireMock";
        String adviceSlipResponse = """
            {
                "slip": {
                    "id": 123,
                    "advice": "%s"
                }
            }
            """.formatted(adviceText);

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/advice"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(adviceSlipResponse)));

        String requestBody = "{\"please\": \"anything\"}";
        HttpRequest<String> request = HttpRequest.POST("/api/v1/advice", requestBody)
                .header("Content-Type", "application/json");

        ResponseDto responseDto = client.toBlocking()
                .retrieve(request, ResponseDto.class);

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getAdvice()).isEqualTo(adviceText);

        WireMock.verify(WireMock.getRequestedFor(WireMock.urlEqualTo("/advice")));
    }
}