package ai.realworld.service.mapper;

import ai.realworld.domain.AlPyuJoker;
import ai.realworld.domain.AlPyuThomasWayne;
import ai.realworld.service.dto.AlPyuJokerDTO;
import ai.realworld.service.dto.AlPyuThomasWayneDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlPyuThomasWayne} and its DTO {@link AlPyuThomasWayneDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlPyuThomasWayneMapper extends EntityMapper<AlPyuThomasWayneDTO, AlPyuThomasWayne> {
    @Mapping(target = "booking", source = "booking", qualifiedByName = "alPyuJokerId")
    AlPyuThomasWayneDTO toDto(AlPyuThomasWayne s);

    @Named("alPyuJokerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlPyuJokerDTO toDtoAlPyuJokerId(AlPyuJoker alPyuJoker);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
