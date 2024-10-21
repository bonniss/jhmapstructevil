package ai.realworld.service.mapper;

import ai.realworld.domain.AlLadyGaga;
import ai.realworld.domain.AndreiRightHand;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.service.dto.AlLadyGagaDTO;
import ai.realworld.service.dto.AndreiRightHandDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.MetaverseDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlLadyGaga} and its DTO {@link AlLadyGagaDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlLadyGagaMapper extends EntityMapper<AlLadyGagaDTO, AlLadyGaga> {
    @Mapping(target = "address", source = "address", qualifiedByName = "andreiRightHandId")
    @Mapping(target = "avatar", source = "avatar", qualifiedByName = "metaverseId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlLadyGagaDTO toDto(AlLadyGaga s);

    @Named("andreiRightHandId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AndreiRightHandDTO toDtoAndreiRightHandId(AndreiRightHand andreiRightHand);

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
