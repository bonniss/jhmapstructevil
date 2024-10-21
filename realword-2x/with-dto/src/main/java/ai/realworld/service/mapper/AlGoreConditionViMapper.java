package ai.realworld.service.mapper;

import ai.realworld.domain.AlGore;
import ai.realworld.domain.AlGoreConditionVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlGoreConditionViDTO;
import ai.realworld.service.dto.AlGoreDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlGoreConditionVi} and its DTO {@link AlGoreConditionViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlGoreConditionViMapper extends EntityMapper<AlGoreConditionViDTO, AlGoreConditionVi> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "alGoreId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlGoreConditionViDTO toDto(AlGoreConditionVi s);

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
