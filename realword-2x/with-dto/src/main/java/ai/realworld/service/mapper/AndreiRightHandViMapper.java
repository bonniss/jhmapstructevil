package ai.realworld.service.mapper;

import ai.realworld.domain.AndreiRightHandVi;
import ai.realworld.domain.AntonioBanderasVi;
import ai.realworld.service.dto.AndreiRightHandViDTO;
import ai.realworld.service.dto.AntonioBanderasViDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AndreiRightHandVi} and its DTO {@link AndreiRightHandViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AndreiRightHandViMapper extends EntityMapper<AndreiRightHandViDTO, AndreiRightHandVi> {
    @Mapping(target = "country", source = "country", qualifiedByName = "antonioBanderasViId")
    @Mapping(target = "province", source = "province", qualifiedByName = "antonioBanderasViId")
    @Mapping(target = "district", source = "district", qualifiedByName = "antonioBanderasViId")
    @Mapping(target = "ward", source = "ward", qualifiedByName = "antonioBanderasViId")
    AndreiRightHandViDTO toDto(AndreiRightHandVi s);

    @Named("antonioBanderasViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AntonioBanderasViDTO toDtoAntonioBanderasViId(AntonioBanderasVi antonioBanderasVi);
}
