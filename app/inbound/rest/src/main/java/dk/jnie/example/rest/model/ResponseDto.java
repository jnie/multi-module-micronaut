package dk.jnie.example.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;

@Data
@Serdeable
public class ResponseDto {
    @JsonProperty("advice")
    String advice;
}