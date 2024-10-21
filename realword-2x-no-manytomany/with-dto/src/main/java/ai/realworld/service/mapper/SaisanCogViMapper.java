package ai.realworld.service.mapper;

import ai.realworld.domain.SaisanCogVi;
import ai.realworld.service.dto.SaisanCogViDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SaisanCogVi} and its DTO {@link SaisanCogViDTO}.
 */
@Mapper(componentModel = "spring")
public interface SaisanCogViMapper extends EntityMapper<SaisanCogViDTO, SaisanCogVi> {}
