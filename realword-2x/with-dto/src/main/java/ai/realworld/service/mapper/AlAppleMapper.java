package ai.realworld.service.mapper;

import ai.realworld.domain.AlAlexType;
import ai.realworld.domain.AlApple;
import ai.realworld.domain.AndreiRightHand;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.service.dto.AlAlexTypeDTO;
import ai.realworld.service.dto.AlAppleDTO;
import ai.realworld.service.dto.AndreiRightHandDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.MetaverseDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlApple} and its DTO {@link AlAppleDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlAppleMapper extends EntityMapper<AlAppleDTO, AlApple> {
    @Mapping(target = "address", source = "address", qualifiedByName = "andreiRightHandId")
    @Mapping(target = "agencyType", source = "agencyType", qualifiedByName = "alAlexTypeId")
    @Mapping(target = "logo", source = "logo", qualifiedByName = "metaverseId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlAppleDTO toDto(AlApple s);

    @Named("andreiRightHandId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AndreiRightHandDTO toDtoAndreiRightHandId(AndreiRightHand andreiRightHand);

    @Named("alAlexTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlAlexTypeDTO toDtoAlAlexTypeId(AlAlexType alAlexType);

    @Named("metaverseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MetaverseDTO toDtoMetaverseId(Metaverse metaverse);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
