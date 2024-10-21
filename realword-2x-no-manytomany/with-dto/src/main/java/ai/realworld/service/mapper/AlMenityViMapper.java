package ai.realworld.service.mapper;

import ai.realworld.domain.AlMenityVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlMenityViDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlMenityVi} and its DTO {@link AlMenityViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlMenityViMapper extends EntityMapper<AlMenityViDTO, AlMenityVi> {
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlMenityViDTO toDto(AlMenityVi s);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
