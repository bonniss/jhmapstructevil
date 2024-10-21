package ai.realworld.service.mapper;

import ai.realworld.domain.AlBestTooth;
import ai.realworld.domain.AlLexFerg;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlBestToothDTO;
import ai.realworld.service.dto.AlLexFergDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlBestTooth} and its DTO {@link AlBestToothDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlBestToothMapper extends EntityMapper<AlBestToothDTO, AlBestTooth> {
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    @Mapping(target = "articles", source = "articles", qualifiedByName = "alLexFergIdSet")
    AlBestToothDTO toDto(AlBestTooth s);

    @Mapping(target = "articles", ignore = true)
    @Mapping(target = "removeArticle", ignore = true)
    AlBestTooth toEntity(AlBestToothDTO alBestToothDTO);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    @Named("alLexFergId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlLexFergDTO toDtoAlLexFergId(AlLexFerg alLexFerg);

    @Named("alLexFergIdSet")
    default Set<AlLexFergDTO> toDtoAlLexFergIdSet(Set<AlLexFerg> alLexFerg) {
        return alLexFerg.stream().map(this::toDtoAlLexFergId).collect(Collectors.toSet());
    }

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
