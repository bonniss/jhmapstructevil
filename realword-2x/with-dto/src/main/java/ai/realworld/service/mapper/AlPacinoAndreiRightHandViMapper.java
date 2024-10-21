package ai.realworld.service.mapper;

import ai.realworld.domain.AlPacino;
import ai.realworld.domain.AlPacinoAndreiRightHandVi;
import ai.realworld.domain.AndreiRightHandVi;
import ai.realworld.service.dto.AlPacinoAndreiRightHandViDTO;
import ai.realworld.service.dto.AlPacinoDTO;
import ai.realworld.service.dto.AndreiRightHandViDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlPacinoAndreiRightHandVi} and its DTO {@link AlPacinoAndreiRightHandViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlPacinoAndreiRightHandViMapper extends EntityMapper<AlPacinoAndreiRightHandViDTO, AlPacinoAndreiRightHandVi> {
    @Mapping(target = "user", source = "user", qualifiedByName = "alPacinoId")
    @Mapping(target = "address", source = "address", qualifiedByName = "andreiRightHandViId")
    AlPacinoAndreiRightHandViDTO toDto(AlPacinoAndreiRightHandVi s);

    @Named("alPacinoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlPacinoDTO toDtoAlPacinoId(AlPacino alPacino);

    @Named("andreiRightHandViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AndreiRightHandViDTO toDtoAndreiRightHandViId(AndreiRightHandVi andreiRightHandVi);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
