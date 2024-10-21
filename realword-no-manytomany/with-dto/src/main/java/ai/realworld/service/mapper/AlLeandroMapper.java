package ai.realworld.service.mapper;

import ai.realworld.domain.AlLeandro;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.service.dto.AlLeandroDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.MetaverseDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlLeandro} and its DTO {@link AlLeandroDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlLeandroMapper extends EntityMapper<AlLeandroDTO, AlLeandro> {
    @Mapping(target = "programBackground", source = "programBackground", qualifiedByName = "metaverseId")
    @Mapping(target = "wheelBackground", source = "wheelBackground", qualifiedByName = "metaverseId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlLeandroDTO toDto(AlLeandro s);

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
