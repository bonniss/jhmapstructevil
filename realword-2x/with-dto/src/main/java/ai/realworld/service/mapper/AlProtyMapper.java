package ai.realworld.service.mapper;

import ai.realworld.domain.AlApple;
import ai.realworld.domain.AlProPro;
import ai.realworld.domain.AlProty;
import ai.realworld.domain.AlPyuJoker;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.service.dto.AlAppleDTO;
import ai.realworld.service.dto.AlProProDTO;
import ai.realworld.service.dto.AlProtyDTO;
import ai.realworld.service.dto.AlPyuJokerDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.MetaverseDTO;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
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
    @Mapping(target = "images", source = "images", qualifiedByName = "metaverseIdSet")
    @Mapping(target = "bookings", source = "bookings", qualifiedByName = "alPyuJokerIdSet")
    AlProtyDTO toDto(AlProty s);

    @Mapping(target = "removeImage", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "removeBooking", ignore = true)
    AlProty toEntity(AlProtyDTO alProtyDTO);

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

    @Named("metaverseIdSet")
    default Set<MetaverseDTO> toDtoMetaverseIdSet(Set<Metaverse> metaverse) {
        return metaverse.stream().map(this::toDtoMetaverseId).collect(Collectors.toSet());
    }

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    @Named("alPyuJokerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlPyuJokerDTO toDtoAlPyuJokerId(AlPyuJoker alPyuJoker);

    @Named("alPyuJokerIdSet")
    default Set<AlPyuJokerDTO> toDtoAlPyuJokerIdSet(Set<AlPyuJoker> alPyuJoker) {
        return alPyuJoker.stream().map(this::toDtoAlPyuJokerId).collect(Collectors.toSet());
    }

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
