package ai.realworld.service.mapper;

import ai.realworld.domain.AlVueVueUsage;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlVueVueUsageDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlVueVueUsage} and its DTO {@link AlVueVueUsageDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlVueVueUsageMapper extends EntityMapper<AlVueVueUsageDTO, AlVueVueUsage> {
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlVueVueUsageDTO toDto(AlVueVueUsage s);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
