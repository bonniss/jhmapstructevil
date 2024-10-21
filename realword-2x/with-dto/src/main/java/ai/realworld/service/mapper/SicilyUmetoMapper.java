package ai.realworld.service.mapper;

import ai.realworld.domain.SicilyUmeto;
import ai.realworld.service.dto.SicilyUmetoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SicilyUmeto} and its DTO {@link SicilyUmetoDTO}.
 */
@Mapper(componentModel = "spring")
public interface SicilyUmetoMapper extends EntityMapper<SicilyUmetoDTO, SicilyUmeto> {}
