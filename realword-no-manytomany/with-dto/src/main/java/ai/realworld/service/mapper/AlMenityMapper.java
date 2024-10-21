package ai.realworld.service.mapper;

import ai.realworld.domain.AlMenity;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlMenityDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlMenity} and its DTO {@link AlMenityDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlMenityMapper extends EntityMapper<AlMenityDTO, AlMenity> {
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlMenityDTO toDto(AlMenity s);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
