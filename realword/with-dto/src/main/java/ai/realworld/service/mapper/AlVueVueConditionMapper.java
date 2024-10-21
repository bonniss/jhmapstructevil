package ai.realworld.service.mapper;

import ai.realworld.domain.AlVueVue;
import ai.realworld.domain.AlVueVueCondition;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlVueVueConditionDTO;
import ai.realworld.service.dto.AlVueVueDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlVueVueCondition} and its DTO {@link AlVueVueConditionDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlVueVueConditionMapper extends EntityMapper<AlVueVueConditionDTO, AlVueVueCondition> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "alVueVueId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlVueVueConditionDTO toDto(AlVueVueCondition s);

    @Named("alVueVueId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlVueVueDTO toDtoAlVueVueId(AlVueVue alVueVue);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
