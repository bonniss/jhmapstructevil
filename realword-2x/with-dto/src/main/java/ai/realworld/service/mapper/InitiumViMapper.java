package ai.realworld.service.mapper;

import ai.realworld.domain.InitiumVi;
import ai.realworld.service.dto.InitiumViDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InitiumVi} and its DTO {@link InitiumViDTO}.
 */
@Mapper(componentModel = "spring")
public interface InitiumViMapper extends EntityMapper<InitiumViDTO, InitiumVi> {}
