package ai.realworld.service.mapper;

import ai.realworld.domain.AlLadyGaga;
import ai.realworld.domain.AlMenity;
import ai.realworld.domain.AlProPro;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.service.dto.AlLadyGagaDTO;
import ai.realworld.service.dto.AlMenityDTO;
import ai.realworld.service.dto.AlProProDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.MetaverseDTO;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlProPro} and its DTO {@link AlProProDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlProProMapper extends EntityMapper<AlProProDTO, AlProPro> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "alProProId")
    @Mapping(target = "project", source = "project", qualifiedByName = "alLadyGagaId")
    @Mapping(target = "avatar", source = "avatar", qualifiedByName = "metaverseId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    @Mapping(target = "amenities", source = "amenities", qualifiedByName = "alMenityIdSet")
    @Mapping(target = "images", source = "images", qualifiedByName = "metaverseIdSet")
    AlProProDTO toDto(AlProPro s);

    @Mapping(target = "removeAmenity", ignore = true)
    @Mapping(target = "removeImage", ignore = true)
    AlProPro toEntity(AlProProDTO alProProDTO);

    @Named("alProProId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlProProDTO toDtoAlProProId(AlProPro alProPro);

    @Named("alLadyGagaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlLadyGagaDTO toDtoAlLadyGagaId(AlLadyGaga alLadyGaga);

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

    @Named("alMenityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlMenityDTO toDtoAlMenityId(AlMenity alMenity);

    @Named("alMenityIdSet")
    default Set<AlMenityDTO> toDtoAlMenityIdSet(Set<AlMenity> alMenity) {
        return alMenity.stream().map(this::toDtoAlMenityId).collect(Collectors.toSet());
    }

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
