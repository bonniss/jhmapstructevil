package ai.realworld.service.mapper;

import ai.realworld.domain.AlAppleVi;
import ai.realworld.domain.AlProProVi;
import ai.realworld.domain.AlProtyVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.service.dto.AlAppleViDTO;
import ai.realworld.service.dto.AlProProViDTO;
import ai.realworld.service.dto.AlProtyViDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.MetaverseDTO;
import java.util.Objects;
import java.util.UUID;
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
    AlProtyViDTO toDto(AlProtyVi s);

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

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
