package ai.realworld.service.mapper;

import ai.realworld.domain.AndreiRightHand;
import ai.realworld.domain.AntonioBanderas;
import ai.realworld.service.dto.AndreiRightHandDTO;
import ai.realworld.service.dto.AntonioBanderasDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AndreiRightHand} and its DTO {@link AndreiRightHandDTO}.
 */
@Mapper(componentModel = "spring")
public interface AndreiRightHandMapper extends EntityMapper<AndreiRightHandDTO, AndreiRightHand> {
    @Mapping(target = "country", source = "country", qualifiedByName = "antonioBanderasId")
    @Mapping(target = "province", source = "province", qualifiedByName = "antonioBanderasId")
    @Mapping(target = "district", source = "district", qualifiedByName = "antonioBanderasId")
    @Mapping(target = "ward", source = "ward", qualifiedByName = "antonioBanderasId")
    AndreiRightHandDTO toDto(AndreiRightHand s);

    @Named("antonioBanderasId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AntonioBanderasDTO toDtoAntonioBanderasId(AntonioBanderas antonioBanderas);
}
