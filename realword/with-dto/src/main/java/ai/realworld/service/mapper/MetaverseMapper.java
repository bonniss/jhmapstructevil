package ai.realworld.service.mapper;

import ai.realworld.domain.AlProPro;
import ai.realworld.domain.AlProty;
import ai.realworld.domain.Metaverse;
import ai.realworld.service.dto.AlProProDTO;
import ai.realworld.service.dto.AlProtyDTO;
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
    MetaverseDTO toDto(Metaverse s);

    @Mapping(target = "alProPros", ignore = true)
    @Mapping(target = "removeAlProPro", ignore = true)
    @Mapping(target = "alProties", ignore = true)
    @Mapping(target = "removeAlProty", ignore = true)
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

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
