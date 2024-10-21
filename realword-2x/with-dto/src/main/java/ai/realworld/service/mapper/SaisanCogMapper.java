package ai.realworld.service.mapper;

import ai.realworld.domain.SaisanCog;
import ai.realworld.service.dto.SaisanCogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SaisanCog} and its DTO {@link SaisanCogDTO}.
 */
@Mapper(componentModel = "spring")
public interface SaisanCogMapper extends EntityMapper<SaisanCogDTO, SaisanCog> {}
