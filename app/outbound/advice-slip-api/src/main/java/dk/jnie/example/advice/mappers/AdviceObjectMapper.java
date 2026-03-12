package dk.jnie.example.advice.mappers;

import dk.jnie.example.advice.model.AdviceResponse;
import dk.jnie.example.domain.model.MultiAggregate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Optional;

@Mapper(componentModel = "cdi")
public interface AdviceObjectMapper {

    @Mapping(target="answer", source="slip.advice")
    MultiAggregate toDomain(AdviceResponse response);
}