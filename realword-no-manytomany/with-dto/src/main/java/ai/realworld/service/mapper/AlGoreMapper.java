package ai.realworld.service.mapper;

import ai.realworld.domain.AlBetonamuRelation;
import ai.realworld.domain.AlGore;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlBetonamuRelationDTO;
import ai.realworld.service.dto.AlGoreDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlGore} and its DTO {@link AlGoreDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlGoreMapper extends EntityMapper<AlGoreDTO, AlGore> {
    @Mapping(target = "bizRelation", source = "bizRelation", qualifiedByName = "alBetonamuRelationId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlGoreDTO toDto(AlGore s);

    @Named("alBetonamuRelationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlBetonamuRelationDTO toDtoAlBetonamuRelationId(AlBetonamuRelation alBetonamuRelation);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
