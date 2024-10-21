package ai.realworld.service.mapper;

import ai.realworld.domain.AlGore;
import ai.realworld.domain.AlGoreCondition;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlGoreConditionDTO;
import ai.realworld.service.dto.AlGoreDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlGoreCondition} and its DTO {@link AlGoreConditionDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlGoreConditionMapper extends EntityMapper<AlGoreConditionDTO, AlGoreCondition> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "alGoreId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlGoreConditionDTO toDto(AlGoreCondition s);

    @Named("alGoreId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlGoreDTO toDtoAlGoreId(AlGore alGore);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
