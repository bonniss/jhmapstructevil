package ai.realworld.service.mapper;

import ai.realworld.domain.Magharetti;
import ai.realworld.service.dto.MagharettiDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Magharetti} and its DTO {@link MagharettiDTO}.
 */
@Mapper(componentModel = "spring")
public interface MagharettiMapper extends EntityMapper<MagharettiDTO, Magharetti> {}
