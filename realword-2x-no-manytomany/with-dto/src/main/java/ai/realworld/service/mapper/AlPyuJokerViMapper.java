package ai.realworld.service.mapper;

import ai.realworld.domain.AlPacino;
import ai.realworld.domain.AlPyuJokerVi;
import ai.realworld.domain.EdSheeranVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlPacinoDTO;
import ai.realworld.service.dto.AlPyuJokerViDTO;
import ai.realworld.service.dto.EdSheeranViDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlPyuJokerVi} and its DTO {@link AlPyuJokerViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlPyuJokerViMapper extends EntityMapper<AlPyuJokerViDTO, AlPyuJokerVi> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "alPacinoId")
    @Mapping(target = "personInCharge", source = "personInCharge", qualifiedByName = "edSheeranViId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlPyuJokerViDTO toDto(AlPyuJokerVi s);

    @Named("alPacinoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlPacinoDTO toDtoAlPacinoId(AlPacino alPacino);

    @Named("edSheeranViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EdSheeranViDTO toDtoEdSheeranViId(EdSheeranVi edSheeranVi);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
