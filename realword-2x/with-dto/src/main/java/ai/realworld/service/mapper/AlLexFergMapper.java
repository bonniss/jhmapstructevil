package ai.realworld.service.mapper;

import ai.realworld.domain.AlBestTooth;
import ai.realworld.domain.AlCatalina;
import ai.realworld.domain.AlLexFerg;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.service.dto.AlBestToothDTO;
import ai.realworld.service.dto.AlCatalinaDTO;
import ai.realworld.service.dto.AlLexFergDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.MetaverseDTO;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlLexFerg} and its DTO {@link AlLexFergDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlLexFergMapper extends EntityMapper<AlLexFergDTO, AlLexFerg> {
    @Mapping(target = "avatar", source = "avatar", qualifiedByName = "metaverseId")
    @Mapping(target = "category", source = "category", qualifiedByName = "alCatalinaId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "alBestToothIdSet")
    AlLexFergDTO toDto(AlLexFerg s);

    @Mapping(target = "removeTag", ignore = true)
    AlLexFerg toEntity(AlLexFergDTO alLexFergDTO);

    @Named("metaverseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MetaverseDTO toDtoMetaverseId(Metaverse metaverse);

    @Named("alCatalinaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlCatalinaDTO toDtoAlCatalinaId(AlCatalina alCatalina);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    @Named("alBestToothId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlBestToothDTO toDtoAlBestToothId(AlBestTooth alBestTooth);

    @Named("alBestToothIdSet")
    default Set<AlBestToothDTO> toDtoAlBestToothIdSet(Set<AlBestTooth> alBestTooth) {
        return alBestTooth.stream().map(this::toDtoAlBestToothId).collect(Collectors.toSet());
    }

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
