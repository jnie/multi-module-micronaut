package dk.jnie.example.mappers;

import dk.jnie.example.rest.mappers.RestMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for RestMapper.
 * Tests the mapping between REST DTOs and domain objects.
 * Note: MapStruct generates implementation at compile time - we test the contract via integration.
 */
@DisplayName("RestMapper Tests")
class RestMapperTest {

    @Test
    @DisplayName("RestMapper should exist and be an interface")
    void restMapper_ShouldExistAndBeInterface() throws Exception {
        Class<?> mapperClass = Class.forName("dk.jnie.example.rest.mappers.RestMapper");
        assertThat(mapperClass.isInterface()).isTrue();
        
        Class<?> mapperImplClass = Class.forName("dk.jnie.example.rest.mappers.RestMapperImpl");
        assertThat(mapperImplClass).isNotNull();
    }
}
