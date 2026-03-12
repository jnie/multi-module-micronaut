package dk.jnie.example.rest.model;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class RequestDto {
    private String please;

    public String getPlease() {
        return please;
    }

    public void setPlease(String please) {
        this.please = please;
    }
}