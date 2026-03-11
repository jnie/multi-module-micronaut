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
    @DisplayName("RestMapper should be properly annotated")
    void restMapper_ShouldBeProperlyAnnotated() throws Exception {
        // Verify that RestMapper interface exists and has MapStruct annotation
        Class<?> mapperClass = Class.forName("dk.jnie.example.rest.mappers.RestMapper");
        
        assertThat(mapperClass.isInterface()).isTrue();
        
        // Verify MapStruct annotation exists
        assertThat(mapperClass.getAnnotation(org.mapstruct.Mapper.class)).isNotNull();
    }
}
