package ai.realworld.service.mapper;

import ai.realworld.domain.AlVueVueViUsage;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlVueVueViUsageDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlVueVueViUsage} and its DTO {@link AlVueVueViUsageDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlVueVueViUsageMapper extends EntityMapper<AlVueVueViUsageDTO, AlVueVueViUsage> {
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlVueVueViUsageDTO toDto(AlVueVueViUsage s);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
