package ai.realworld.service.mapper;

import ai.realworld.domain.PamelaLouis;
import ai.realworld.service.dto.PamelaLouisDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PamelaLouis} and its DTO {@link PamelaLouisDTO}.
 */
@Mapper(componentModel = "spring")
public interface PamelaLouisMapper extends EntityMapper<PamelaLouisDTO, PamelaLouis> {}
