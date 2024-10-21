package ai.realworld.service.mapper;

import ai.realworld.domain.AlProtyVi;
import ai.realworld.domain.AlPyuDjibrilVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlProtyViDTO;
import ai.realworld.service.dto.AlPyuDjibrilViDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlPyuDjibrilVi} and its DTO {@link AlPyuDjibrilViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlPyuDjibrilViMapper extends EntityMapper<AlPyuDjibrilViDTO, AlPyuDjibrilVi> {
    @Mapping(target = "property", source = "property", qualifiedByName = "alProtyViId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlPyuDjibrilViDTO toDto(AlPyuDjibrilVi s);

    @Named("alProtyViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlProtyViDTO toDtoAlProtyViId(AlProtyVi alProtyVi);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
