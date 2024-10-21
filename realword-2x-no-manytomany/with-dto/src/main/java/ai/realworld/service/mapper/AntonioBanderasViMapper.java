package ai.realworld.service.mapper;

import ai.realworld.domain.AntonioBanderasVi;
import ai.realworld.service.dto.AntonioBanderasViDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AntonioBanderasVi} and its DTO {@link AntonioBanderasViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AntonioBanderasViMapper extends EntityMapper<AntonioBanderasViDTO, AntonioBanderasVi> {
    @Mapping(target = "current", source = "current", qualifiedByName = "antonioBanderasViId")
    @Mapping(target = "parent", source = "parent", qualifiedByName = "antonioBanderasViCode")
    AntonioBanderasViDTO toDto(AntonioBanderasVi s);

    @Named("antonioBanderasViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AntonioBanderasViDTO toDtoAntonioBanderasViId(AntonioBanderasVi antonioBanderasVi);

    @Named("antonioBanderasViCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    AntonioBanderasViDTO toDtoAntonioBanderasViCode(AntonioBanderasVi antonioBanderasVi);
}
