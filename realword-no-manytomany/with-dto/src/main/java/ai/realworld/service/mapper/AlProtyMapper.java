package ai.realworld.service.mapper;

import ai.realworld.domain.AlApple;
import ai.realworld.domain.AlProPro;
import ai.realworld.domain.AlProty;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.service.dto.AlAppleDTO;
import ai.realworld.service.dto.AlProProDTO;
import ai.realworld.service.dto.AlProtyDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.MetaverseDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlProty} and its DTO {@link AlProtyDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlProtyMapper extends EntityMapper<AlProtyDTO, AlProty> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "alProtyId")
    @Mapping(target = "operator", source = "operator", qualifiedByName = "alAppleId")
    @Mapping(target = "propertyProfile", source = "propertyProfile", qualifiedByName = "alProProId")
    @Mapping(target = "avatar", source = "avatar", qualifiedByName = "metaverseId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlProtyDTO toDto(AlProty s);

    @Named("alProtyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlProtyDTO toDtoAlProtyId(AlProty alProty);

    @Named("alAppleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlAppleDTO toDtoAlAppleId(AlApple alApple);

    @Named("alProProId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlProProDTO toDtoAlProProId(AlProPro alProPro);

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
