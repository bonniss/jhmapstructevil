package ai.realworld.service.mapper;

import ai.realworld.domain.AlActiso;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlActisoDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlActiso} and its DTO {@link AlActisoDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlActisoMapper extends EntityMapper<AlActisoDTO, AlActiso> {
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlActisoDTO toDto(AlActiso s);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
