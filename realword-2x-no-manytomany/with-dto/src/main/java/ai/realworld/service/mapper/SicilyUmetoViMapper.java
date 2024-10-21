package ai.realworld.service.mapper;

import ai.realworld.domain.SicilyUmetoVi;
import ai.realworld.service.dto.SicilyUmetoViDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SicilyUmetoVi} and its DTO {@link SicilyUmetoViDTO}.
 */
@Mapper(componentModel = "spring")
public interface SicilyUmetoViMapper extends EntityMapper<SicilyUmetoViDTO, SicilyUmetoVi> {}
