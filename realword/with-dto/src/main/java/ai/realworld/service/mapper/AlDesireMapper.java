package ai.realworld.service.mapper;

import ai.realworld.domain.AlDesire;
import ai.realworld.domain.AlLeandro;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.service.dto.AlDesireDTO;
import ai.realworld.service.dto.AlLeandroDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.MetaverseDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlDesire} and its DTO {@link AlDesireDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlDesireMapper extends EntityMapper<AlDesireDTO, AlDesire> {
    @Mapping(target = "image", source = "image", qualifiedByName = "metaverseId")
    @Mapping(target = "miniGame", source = "miniGame", qualifiedByName = "alLeandroId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlDesireDTO toDto(AlDesire s);

    @Named("metaverseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MetaverseDTO toDtoMetaverseId(Metaverse metaverse);

    @Named("alLeandroId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlLeandroDTO toDtoAlLeandroId(AlLeandro alLeandro);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
