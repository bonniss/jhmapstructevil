package ai.realworld.service.mapper;

import ai.realworld.domain.AlPyuJokerVi;
import ai.realworld.domain.AlPyuThomasWayneVi;
import ai.realworld.service.dto.AlPyuJokerViDTO;
import ai.realworld.service.dto.AlPyuThomasWayneViDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlPyuThomasWayneVi} and its DTO {@link AlPyuThomasWayneViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlPyuThomasWayneViMapper extends EntityMapper<AlPyuThomasWayneViDTO, AlPyuThomasWayneVi> {
    @Mapping(target = "booking", source = "booking", qualifiedByName = "alPyuJokerViId")
    AlPyuThomasWayneViDTO toDto(AlPyuThomasWayneVi s);

    @Named("alPyuJokerViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlPyuJokerViDTO toDtoAlPyuJokerViId(AlPyuJokerVi alPyuJokerVi);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
