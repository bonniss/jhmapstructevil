package ai.realworld.service.mapper;

import ai.realworld.domain.AlLadyGagaVi;
import ai.realworld.domain.AlMenityVi;
import ai.realworld.domain.AlProProVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.service.dto.AlLadyGagaViDTO;
import ai.realworld.service.dto.AlMenityViDTO;
import ai.realworld.service.dto.AlProProViDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.MetaverseDTO;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlProProVi} and its DTO {@link AlProProViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlProProViMapper extends EntityMapper<AlProProViDTO, AlProProVi> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "alProProViId")
    @Mapping(target = "project", source = "project", qualifiedByName = "alLadyGagaViId")
    @Mapping(target = "avatar", source = "avatar", qualifiedByName = "metaverseId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    @Mapping(target = "amenities", source = "amenities", qualifiedByName = "alMenityViIdSet")
    @Mapping(target = "images", source = "images", qualifiedByName = "metaverseIdSet")
    AlProProViDTO toDto(AlProProVi s);

    @Mapping(target = "removeAmenity", ignore = true)
    @Mapping(target = "removeImage", ignore = true)
    AlProProVi toEntity(AlProProViDTO alProProViDTO);

    @Named("alProProViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlProProViDTO toDtoAlProProViId(AlProProVi alProProVi);

    @Named("alLadyGagaViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlLadyGagaViDTO toDtoAlLadyGagaViId(AlLadyGagaVi alLadyGagaVi);

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

    @Named("alMenityViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlMenityViDTO toDtoAlMenityViId(AlMenityVi alMenityVi);

    @Named("alMenityViIdSet")
    default Set<AlMenityViDTO> toDtoAlMenityViIdSet(Set<AlMenityVi> alMenityVi) {
        return alMenityVi.stream().map(this::toDtoAlMenityViId).collect(Collectors.toSet());
    }

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
