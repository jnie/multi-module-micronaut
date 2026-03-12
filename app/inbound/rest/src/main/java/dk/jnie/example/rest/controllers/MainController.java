package dk.jnie.example.rest.controllers;

import dk.jnie.example.rest.mappers.RestMapper;
import dk.jnie.example.rest.model.RequestDto;
import dk.jnie.example.rest.model.ResponseDto;
import dk.jnie.example.domain.model.DomainRequest;
import dk.jnie.example.domain.model.DomainResponse;
import dk.jnie.example.domain.services.OurService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Produces;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.serde.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/api/v1/advice")
@Tag(name = "Main API", description = "Call for an advice")
public class MainController {

    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);

    private final OurService ourService;
    private final RestMapper restMapper;
    private final ObjectMapper objectMapper;

    @Inject
    public MainController(OurService ourService, RestMapper restMapper, ObjectMapper objectMapper) {
        this.ourService = ourService;
        this.restMapper = restMapper;
        this.objectMapper = objectMapper;
    }

    @Post
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getAdvice(@Body RequestDto request) throws Exception {
        String question = request.getPlease() != null ? request.getPlease() : "default";
        
        DomainResponse response = ourService.getAnAdvice(
            DomainRequest.builder().question(question).build()).join();
        
        ResponseDto responseDto = new ResponseDto();
        responseDto.setAdvice(response.getAnswer());
        
        String json = objectMapper.writeValueAsString(responseDto);
        LOG.info("Returning JSON: {}", json);
        return json;
    }
}