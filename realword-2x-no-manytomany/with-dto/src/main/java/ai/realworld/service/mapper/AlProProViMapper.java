package ai.realworld.service.mapper;

import ai.realworld.domain.AlLadyGagaVi;
import ai.realworld.domain.AlProProVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.service.dto.AlLadyGagaViDTO;
import ai.realworld.service.dto.AlProProViDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.MetaverseDTO;
import java.util.Objects;
import java.util.UUID;
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
    AlProProViDTO toDto(AlProProVi s);

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

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
