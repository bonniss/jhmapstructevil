package ai.realworld.service.mapper;

import ai.realworld.domain.Metaverse;
import ai.realworld.service.dto.MetaverseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Metaverse} and its DTO {@link MetaverseDTO}.
 */
@Mapper(componentModel = "spring")
public interface MetaverseMapper extends EntityMapper<MetaverseDTO, Metaverse> {}
