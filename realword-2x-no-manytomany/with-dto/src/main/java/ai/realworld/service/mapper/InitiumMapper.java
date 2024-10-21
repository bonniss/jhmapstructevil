package ai.realworld.service.mapper;

import ai.realworld.domain.Initium;
import ai.realworld.service.dto.InitiumDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Initium} and its DTO {@link InitiumDTO}.
 */
@Mapper(componentModel = "spring")
public interface InitiumMapper extends EntityMapper<InitiumDTO, Initium> {}
