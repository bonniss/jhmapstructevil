package ai.realworld.service.mapper;

import ai.realworld.domain.AlPacino;
import ai.realworld.domain.AlPyuJoker;
import ai.realworld.domain.EdSheeran;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlPacinoDTO;
import ai.realworld.service.dto.AlPyuJokerDTO;
import ai.realworld.service.dto.EdSheeranDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlPyuJoker} and its DTO {@link AlPyuJokerDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlPyuJokerMapper extends EntityMapper<AlPyuJokerDTO, AlPyuJoker> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "alPacinoId")
    @Mapping(target = "personInCharge", source = "personInCharge", qualifiedByName = "edSheeranId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlPyuJokerDTO toDto(AlPyuJoker s);

    @Named("alPacinoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlPacinoDTO toDtoAlPacinoId(AlPacino alPacino);

    @Named("edSheeranId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EdSheeranDTO toDtoEdSheeranId(EdSheeran edSheeran);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
