package ai.realworld.service.mapper;

import ai.realworld.domain.HashRoss;
import ai.realworld.service.dto.HashRossDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HashRoss} and its DTO {@link HashRossDTO}.
 */
@Mapper(componentModel = "spring")
public interface HashRossMapper extends EntityMapper<HashRossDTO, HashRoss> {}
