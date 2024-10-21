package ai.realworld.service.mapper;

import ai.realworld.domain.AlAlexType;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlAlexTypeDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlAlexType} and its DTO {@link AlAlexTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlAlexTypeMapper extends EntityMapper<AlAlexTypeDTO, AlAlexType> {
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlAlexTypeDTO toDto(AlAlexType s);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
