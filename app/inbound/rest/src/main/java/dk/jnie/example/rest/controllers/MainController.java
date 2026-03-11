package dk.jnie.example.rest.controllers;

import dk.jnie.example.rest.mappers.RestMapper;
import dk.jnie.example.rest.model.RequestDto;
import dk.jnie.example.rest.model.ResponseDto;
import dk.jnie.example.domain.services.OurService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.validation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

@Controller("/api/v1/advice")
@Validated
@Tag(name = "Main API", description = "Call for an advice, this could f.ex be a random advice")
public class MainController {

    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);

    private final OurService ourService;
    private final RestMapper restMapper;

    @Inject
    public MainController(OurService ourService, RestMapper restMapper) {
        this.ourService = ourService;
        this.restMapper = restMapper;
    }

    @Post
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Ask for an advice",
            description = "Usage, use POST verb and the request model to ask for an advice")
    @ApiResponse(responseCode = "200", description = "Success",
            content = @Content(schema = @Schema(implementation = RequestDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public CompletableFuture<ResponseDto> getAdvice(
            @Body @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RequestDto.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "please": "anything"
                                            }
                                            """
                            )
                    )
            )
            RequestDto request) {
        return ourService.getAnAdvice(restMapper.requestDTOToDomain(request))
                .thenApply(restMapper::domainToResponseDto);
    }
}