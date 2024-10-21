package ai.realworld.service.mapper;

import ai.realworld.domain.AlGoreVi;
import ai.realworld.service.dto.AlGoreViDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlGoreVi} and its DTO {@link AlGoreViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlGoreViMapper extends EntityMapper<AlGoreViDTO, AlGoreVi> {}
