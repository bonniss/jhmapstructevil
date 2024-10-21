package ai.realworld.service.mapper;

import ai.realworld.domain.AlLadyGaga;
import ai.realworld.domain.AlProPro;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.service.dto.AlLadyGagaDTO;
import ai.realworld.service.dto.AlProProDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.MetaverseDTO;
import java.util.Objects;
import java.util.UUID;
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
    AlProProDTO toDto(AlProPro s);

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

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
