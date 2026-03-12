package dk.jnie.example.rest.mappers;

import dk.jnie.example.domain.model.DomainRequest;
import dk.jnie.example.domain.model.DomainResponse;
import dk.jnie.example.rest.model.RequestDto;
import dk.jnie.example.rest.model.ResponseDto;
import jakarta.enterprise.context.ApplicationScoped;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-12T10:26:41+0100",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.10 (Ubuntu)"
)
@ApplicationScoped
public class RestMapperImpl implements RestMapper {

    @Override
    public ResponseDto domainToResponseDto(DomainResponse domainResponse) {
        if ( domainResponse == null ) {
            return null;
        }

        ResponseDto responseDto = new ResponseDto();

        responseDto.setAdvice( domainResponse.getAnswer() );

        return responseDto;
    }

    @Override
    public DomainRequest requestDTOToDomain(RequestDto requestDto) {
        if ( requestDto == null ) {
            return null;
        }

        DomainRequest.Builder domainRequest = DomainRequest.builder();

        domainRequest.question( requestDto.getPlease() );

        return domainRequest.build();
    }
}
