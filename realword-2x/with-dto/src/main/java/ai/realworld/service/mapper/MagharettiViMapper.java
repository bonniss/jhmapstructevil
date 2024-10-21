package ai.realworld.service.mapper;

import ai.realworld.domain.MagharettiVi;
import ai.realworld.service.dto.MagharettiViDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MagharettiVi} and its DTO {@link MagharettiViDTO}.
 */
@Mapper(componentModel = "spring")
public interface MagharettiViMapper extends EntityMapper<MagharettiViDTO, MagharettiVi> {}
