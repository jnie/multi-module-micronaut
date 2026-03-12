package dk.jnie.example.advice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

/**
 * Response model for the Advice Slip API.
 * Maps to the JSON structure:
 * {
 *   "slip": {
 *     "id": 125,
 *     "advice": "Why wait until valentines day for a romantic gesture?"
 *   }
 * }
 */
@Serdeable
public class AdviceResponse {

    @JsonProperty("slip")
    private Slip slip;

    public Slip getSlip() {
        return slip;
    }

    public void setSlip(Slip slip) {
        this.slip = slip;
    }

    @Serdeable
    public static class Slip {
        
        @JsonProperty("id")
        private Integer id;
        
        @JsonProperty("advice")
        private String advice;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getAdvice() {
            return advice;
        }

        public void setAdvice(String advice) {
            this.advice = advice;
        }
    }
}