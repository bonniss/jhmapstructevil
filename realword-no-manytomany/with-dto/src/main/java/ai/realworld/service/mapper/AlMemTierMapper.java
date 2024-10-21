package ai.realworld.service.mapper;

import ai.realworld.domain.AlMemTier;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlMemTierDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlMemTier} and its DTO {@link AlMemTierDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlMemTierMapper extends EntityMapper<AlMemTierDTO, AlMemTier> {
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlMemTierDTO toDto(AlMemTier s);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
