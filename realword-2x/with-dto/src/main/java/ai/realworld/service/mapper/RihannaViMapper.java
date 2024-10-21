package ai.realworld.service.mapper;

import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.RihannaVi;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.RihannaViDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RihannaVi} and its DTO {@link RihannaViDTO}.
 */
@Mapper(componentModel = "spring")
public interface RihannaViMapper extends EntityMapper<RihannaViDTO, RihannaVi> {
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    RihannaViDTO toDto(RihannaVi s);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
