package ai.realworld.service.mapper;

import ai.realworld.domain.AlProty;
import ai.realworld.domain.AlPyuDjibril;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlProtyDTO;
import ai.realworld.service.dto.AlPyuDjibrilDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlPyuDjibril} and its DTO {@link AlPyuDjibrilDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlPyuDjibrilMapper extends EntityMapper<AlPyuDjibrilDTO, AlPyuDjibril> {
    @Mapping(target = "property", source = "property", qualifiedByName = "alProtyId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlPyuDjibrilDTO toDto(AlPyuDjibril s);

    @Named("alProtyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlProtyDTO toDtoAlProtyId(AlProty alProty);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
