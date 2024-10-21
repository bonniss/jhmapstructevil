package ai.realworld.service.mapper;

import ai.realworld.domain.AlDesireVi;
import ai.realworld.domain.AlLeandro;
import ai.realworld.domain.Metaverse;
import ai.realworld.service.dto.AlDesireViDTO;
import ai.realworld.service.dto.AlLeandroDTO;
import ai.realworld.service.dto.MetaverseDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlDesireVi} and its DTO {@link AlDesireViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlDesireViMapper extends EntityMapper<AlDesireViDTO, AlDesireVi> {
    @Mapping(target = "image", source = "image", qualifiedByName = "metaverseId")
    @Mapping(target = "maggi", source = "maggi", qualifiedByName = "alLeandroId")
    AlDesireViDTO toDto(AlDesireVi s);

    @Named("metaverseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MetaverseDTO toDtoMetaverseId(Metaverse metaverse);

    @Named("alLeandroId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlLeandroDTO toDtoAlLeandroId(AlLeandro alLeandro);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
