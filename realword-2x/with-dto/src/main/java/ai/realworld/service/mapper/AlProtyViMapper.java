package ai.realworld.service.mapper;

import ai.realworld.domain.AlAppleVi;
import ai.realworld.domain.AlProProVi;
import ai.realworld.domain.AlProtyVi;
import ai.realworld.domain.AlPyuJokerVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.service.dto.AlAppleViDTO;
import ai.realworld.service.dto.AlProProViDTO;
import ai.realworld.service.dto.AlProtyViDTO;
import ai.realworld.service.dto.AlPyuJokerViDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.MetaverseDTO;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlProtyVi} and its DTO {@link AlProtyViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlProtyViMapper extends EntityMapper<AlProtyViDTO, AlProtyVi> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "alProtyViId")
    @Mapping(target = "operator", source = "operator", qualifiedByName = "alAppleViId")
    @Mapping(target = "propertyProfile", source = "propertyProfile", qualifiedByName = "alProProViId")
    @Mapping(target = "avatar", source = "avatar", qualifiedByName = "metaverseId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    @Mapping(target = "images", source = "images", qualifiedByName = "metaverseIdSet")
    @Mapping(target = "bookings", source = "bookings", qualifiedByName = "alPyuJokerViIdSet")
    AlProtyViDTO toDto(AlProtyVi s);

    @Mapping(target = "removeImage", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "removeBooking", ignore = true)
    AlProtyVi toEntity(AlProtyViDTO alProtyViDTO);

    @Named("alProtyViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlProtyViDTO toDtoAlProtyViId(AlProtyVi alProtyVi);

    @Named("alAppleViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlAppleViDTO toDtoAlAppleViId(AlAppleVi alAppleVi);

    @Named("alProProViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlProProViDTO toDtoAlProProViId(AlProProVi alProProVi);

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

    @Named("alPyuJokerViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlPyuJokerViDTO toDtoAlPyuJokerViId(AlPyuJokerVi alPyuJokerVi);

    @Named("alPyuJokerViIdSet")
    default Set<AlPyuJokerViDTO> toDtoAlPyuJokerViIdSet(Set<AlPyuJokerVi> alPyuJokerVi) {
        return alPyuJokerVi.stream().map(this::toDtoAlPyuJokerViId).collect(Collectors.toSet());
    }

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
