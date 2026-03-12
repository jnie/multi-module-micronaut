package dk.jnie.example.advice.mappers;

import dk.jnie.example.advice.model.AdviceResponse;
import dk.jnie.example.domain.model.MultiAggregate;
import jakarta.enterprise.context.ApplicationScoped;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-12T10:26:43+0100",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.10 (Ubuntu)"
)
@ApplicationScoped
public class AdviceObjectMapperImpl implements AdviceObjectMapper {

    @Override
    public MultiAggregate toDomain(AdviceResponse response) {
        if ( response == null ) {
            return null;
        }

        MultiAggregate.Builder multiAggregate = MultiAggregate.builder();

        multiAggregate.answer( responseSlipAdvice( response ) );

        return multiAggregate.build();
    }

    private String responseSlipAdvice(AdviceResponse adviceResponse) {
        AdviceResponse.Slip slip = adviceResponse.getSlip();
        if ( slip == null ) {
            return null;
        }
        return slip.getAdvice();
    }
}
