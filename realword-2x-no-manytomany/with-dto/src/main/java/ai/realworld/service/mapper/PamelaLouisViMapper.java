package ai.realworld.service.mapper;

import ai.realworld.domain.PamelaLouisVi;
import ai.realworld.service.dto.PamelaLouisViDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PamelaLouisVi} and its DTO {@link PamelaLouisViDTO}.
 */
@Mapper(componentModel = "spring")
public interface PamelaLouisViMapper extends EntityMapper<PamelaLouisViDTO, PamelaLouisVi> {}
