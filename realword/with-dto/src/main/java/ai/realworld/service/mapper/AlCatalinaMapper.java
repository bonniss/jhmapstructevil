package ai.realworld.service.mapper;

import ai.realworld.domain.AlCatalina;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.service.dto.AlCatalinaDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.MetaverseDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlCatalina} and its DTO {@link AlCatalinaDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlCatalinaMapper extends EntityMapper<AlCatalinaDTO, AlCatalina> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "alCatalinaId")
    @Mapping(target = "avatar", source = "avatar", qualifiedByName = "metaverseId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlCatalinaDTO toDto(AlCatalina s);

    @Named("alCatalinaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlCatalinaDTO toDtoAlCatalinaId(AlCatalina alCatalina);

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
