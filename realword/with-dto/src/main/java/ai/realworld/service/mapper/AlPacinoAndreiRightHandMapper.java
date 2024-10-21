package ai.realworld.service.mapper;

import ai.realworld.domain.AlPacino;
import ai.realworld.domain.AlPacinoAndreiRightHand;
import ai.realworld.domain.AndreiRightHand;
import ai.realworld.service.dto.AlPacinoAndreiRightHandDTO;
import ai.realworld.service.dto.AlPacinoDTO;
import ai.realworld.service.dto.AndreiRightHandDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlPacinoAndreiRightHand} and its DTO {@link AlPacinoAndreiRightHandDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlPacinoAndreiRightHandMapper extends EntityMapper<AlPacinoAndreiRightHandDTO, AlPacinoAndreiRightHand> {
    @Mapping(target = "user", source = "user", qualifiedByName = "alPacinoId")
    @Mapping(target = "address", source = "address", qualifiedByName = "andreiRightHandId")
    AlPacinoAndreiRightHandDTO toDto(AlPacinoAndreiRightHand s);

    @Named("alPacinoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlPacinoDTO toDtoAlPacinoId(AlPacino alPacino);

    @Named("andreiRightHandId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AndreiRightHandDTO toDtoAndreiRightHandId(AndreiRightHand andreiRightHand);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
