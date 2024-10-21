package ai.realworld.service.mapper;

import ai.realworld.domain.AlMemTierVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlMemTierViDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlMemTierVi} and its DTO {@link AlMemTierViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlMemTierViMapper extends EntityMapper<AlMemTierViDTO, AlMemTierVi> {
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlMemTierViDTO toDto(AlMemTierVi s);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
