package ai.realworld.service.mapper;

import ai.realworld.domain.AlSherMale;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlSherMaleDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlSherMale} and its DTO {@link AlSherMaleDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlSherMaleMapper extends EntityMapper<AlSherMaleDTO, AlSherMale> {
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlSherMaleDTO toDto(AlSherMale s);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
