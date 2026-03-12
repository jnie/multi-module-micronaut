package dk.jnie.example.config;

import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;

/**
 * Configuration properties for the application.
 * In Micronaut, we use @Value for property injection (same as Spring).
 */
@Singleton
public class WebClientConfig {

    @Value("${mma.outbound.advice-slip-api.url}")
    private String adviceSlipApiUrl;

    public String getAdviceSlipApiUrl() {
        return adviceSlipApiUrl;
    }
}