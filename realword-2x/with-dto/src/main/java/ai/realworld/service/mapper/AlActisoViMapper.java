package ai.realworld.service.mapper;

import ai.realworld.domain.AlActisoVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlActisoViDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlActisoVi} and its DTO {@link AlActisoViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlActisoViMapper extends EntityMapper<AlActisoViDTO, AlActisoVi> {
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlActisoViDTO toDto(AlActisoVi s);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
