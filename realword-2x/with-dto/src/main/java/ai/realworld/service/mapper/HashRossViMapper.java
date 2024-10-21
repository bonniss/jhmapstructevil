package ai.realworld.service.mapper;

import ai.realworld.domain.HashRossVi;
import ai.realworld.service.dto.HashRossViDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HashRossVi} and its DTO {@link HashRossViDTO}.
 */
@Mapper(componentModel = "spring")
public interface HashRossViMapper extends EntityMapper<HashRossViDTO, HashRossVi> {}
