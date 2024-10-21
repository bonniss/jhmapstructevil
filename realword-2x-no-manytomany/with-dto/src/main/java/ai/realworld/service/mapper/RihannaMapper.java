package ai.realworld.service.mapper;

import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Rihanna;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.RihannaDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Rihanna} and its DTO {@link RihannaDTO}.
 */
@Mapper(componentModel = "spring")
public interface RihannaMapper extends EntityMapper<RihannaDTO, Rihanna> {
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    RihannaDTO toDto(Rihanna s);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
