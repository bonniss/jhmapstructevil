package ai.realworld.service.mapper;

import ai.realworld.domain.AntonioBanderas;
import ai.realworld.service.dto.AntonioBanderasDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AntonioBanderas} and its DTO {@link AntonioBanderasDTO}.
 */
@Mapper(componentModel = "spring")
public interface AntonioBanderasMapper extends EntityMapper<AntonioBanderasDTO, AntonioBanderas> {
    @Mapping(target = "current", source = "current", qualifiedByName = "antonioBanderasId")
    @Mapping(target = "parent", source = "parent", qualifiedByName = "antonioBanderasCode")
    AntonioBanderasDTO toDto(AntonioBanderas s);

    @Named("antonioBanderasId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AntonioBanderasDTO toDtoAntonioBanderasId(AntonioBanderas antonioBanderas);

    @Named("antonioBanderasCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    AntonioBanderasDTO toDtoAntonioBanderasCode(AntonioBanderas antonioBanderas);
}
