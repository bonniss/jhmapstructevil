package ai.realworld.service.mapper;

import ai.realworld.domain.AlLadyGagaVi;
import ai.realworld.domain.AndreiRightHandVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.service.dto.AlLadyGagaViDTO;
import ai.realworld.service.dto.AndreiRightHandViDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.MetaverseDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlLadyGagaVi} and its DTO {@link AlLadyGagaViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlLadyGagaViMapper extends EntityMapper<AlLadyGagaViDTO, AlLadyGagaVi> {
    @Mapping(target = "address", source = "address", qualifiedByName = "andreiRightHandViId")
    @Mapping(target = "avatar", source = "avatar", qualifiedByName = "metaverseId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlLadyGagaViDTO toDto(AlLadyGagaVi s);

    @Named("andreiRightHandViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AndreiRightHandViDTO toDtoAndreiRightHandViId(AndreiRightHandVi andreiRightHandVi);

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
