package ai.realworld.service.mapper;

import ai.realworld.domain.AlSherMaleVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlSherMaleViDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlSherMaleVi} and its DTO {@link AlSherMaleViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlSherMaleViMapper extends EntityMapper<AlSherMaleViDTO, AlSherMaleVi> {
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlSherMaleViDTO toDto(AlSherMaleVi s);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
