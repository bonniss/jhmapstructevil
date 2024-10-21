package ai.realworld.service.mapper;

import ai.realworld.domain.AlLexFergVi;
import ai.realworld.service.dto.AlLexFergViDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlLexFergVi} and its DTO {@link AlLexFergViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlLexFergViMapper extends EntityMapper<AlLexFergViDTO, AlLexFergVi> {}
