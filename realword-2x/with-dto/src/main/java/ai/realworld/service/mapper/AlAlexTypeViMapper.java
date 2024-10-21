package ai.realworld.service.mapper;

import ai.realworld.domain.AlAlexTypeVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlAlexTypeViDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlAlexTypeVi} and its DTO {@link AlAlexTypeViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlAlexTypeViMapper extends EntityMapper<AlAlexTypeViDTO, AlAlexTypeVi> {
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlAlexTypeViDTO toDto(AlAlexTypeVi s);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
