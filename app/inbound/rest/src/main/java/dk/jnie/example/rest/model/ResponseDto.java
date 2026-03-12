package dk.jnie.example.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class ResponseDto {
    @JsonProperty("advice")
    private String advice;

    public ResponseDto() {}

    public ResponseDto(String advice) {
        this.advice = advice;
    }

    @JsonProperty("advice")
    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    @Override
    public String toString() {
        return "ResponseDto{advice='" + advice + "'}";
    }
}