package ai.realworld.service.mapper;

import ai.realworld.domain.AlProPro;
import ai.realworld.domain.AlProProVi;
import ai.realworld.domain.AlProty;
import ai.realworld.domain.AlProtyVi;
import ai.realworld.domain.Metaverse;
import ai.realworld.service.dto.AlProProDTO;
import ai.realworld.service.dto.AlProProViDTO;
import ai.realworld.service.dto.AlProtyDTO;
import ai.realworld.service.dto.AlProtyViDTO;
import ai.realworld.service.dto.MetaverseDTO;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Metaverse} and its DTO {@link MetaverseDTO}.
 */
@Mapper(componentModel = "spring")
public interface MetaverseMapper extends EntityMapper<MetaverseDTO, Metaverse> {
    @Mapping(target = "alProPros", source = "alProPros", qualifiedByName = "alProProIdSet")
    @Mapping(target = "alProties", source = "alProties", qualifiedByName = "alProtyIdSet")
    @Mapping(target = "alProProVis", source = "alProProVis", qualifiedByName = "alProProViIdSet")
    @Mapping(target = "alProtyVis", source = "alProtyVis", qualifiedByName = "alProtyViIdSet")
    MetaverseDTO toDto(Metaverse s);

    @Mapping(target = "alProPros", ignore = true)
    @Mapping(target = "removeAlProPro", ignore = true)
    @Mapping(target = "alProties", ignore = true)
    @Mapping(target = "removeAlProty", ignore = true)
    @Mapping(target = "alProProVis", ignore = true)
    @Mapping(target = "removeAlProProVi", ignore = true)
    @Mapping(target = "alProtyVis", ignore = true)
    @Mapping(target = "removeAlProtyVi", ignore = true)
    Metaverse toEntity(MetaverseDTO metaverseDTO);

    @Named("alProProId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlProProDTO toDtoAlProProId(AlProPro alProPro);

    @Named("alProProIdSet")
    default Set<AlProProDTO> toDtoAlProProIdSet(Set<AlProPro> alProPro) {
        return alProPro.stream().map(this::toDtoAlProProId).collect(Collectors.toSet());
    }

    @Named("alProtyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlProtyDTO toDtoAlProtyId(AlProty alProty);

    @Named("alProtyIdSet")
    default Set<AlProtyDTO> toDtoAlProtyIdSet(Set<AlProty> alProty) {
        return alProty.stream().map(this::toDtoAlProtyId).collect(Collectors.toSet());
    }

    @Named("alProProViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlProProViDTO toDtoAlProProViId(AlProProVi alProProVi);

    @Named("alProProViIdSet")
    default Set<AlProProViDTO> toDtoAlProProViIdSet(Set<AlProProVi> alProProVi) {
        return alProProVi.stream().map(this::toDtoAlProProViId).collect(Collectors.toSet());
    }

    @Named("alProtyViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlProtyViDTO toDtoAlProtyViId(AlProtyVi alProtyVi);

    @Named("alProtyViIdSet")
    default Set<AlProtyViDTO> toDtoAlProtyViIdSet(Set<AlProtyVi> alProtyVi) {
        return alProtyVi.stream().map(this::toDtoAlProtyViId).collect(Collectors.toSet());
    }

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
