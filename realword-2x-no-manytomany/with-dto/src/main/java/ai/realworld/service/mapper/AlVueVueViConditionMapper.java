package ai.realworld.service.mapper;

import ai.realworld.domain.AlVueVueVi;
import ai.realworld.domain.AlVueVueViCondition;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlVueVueViConditionDTO;
import ai.realworld.service.dto.AlVueVueViDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlVueVueViCondition} and its DTO {@link AlVueVueViConditionDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlVueVueViConditionMapper extends EntityMapper<AlVueVueViConditionDTO, AlVueVueViCondition> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "alVueVueViId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlVueVueViConditionDTO toDto(AlVueVueViCondition s);

    @Named("alVueVueViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlVueVueViDTO toDtoAlVueVueViId(AlVueVueVi alVueVueVi);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
