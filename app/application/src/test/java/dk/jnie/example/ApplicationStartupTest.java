package dk.jnie.example;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
@DisplayName("Application Startup Test")
class ApplicationStartupTest {

    @Inject
    EmbeddedApplication<?> application;

    @Test
    @DisplayName("Application should start successfully")
    void applicationShouldStart() {
        assertThat(application.isRunning()).isTrue();
    }
}