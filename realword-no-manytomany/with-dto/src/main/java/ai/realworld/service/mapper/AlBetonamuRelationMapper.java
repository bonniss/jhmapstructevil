package ai.realworld.service.mapper;

import ai.realworld.domain.AlAlexType;
import ai.realworld.domain.AlBetonamuRelation;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlAlexTypeDTO;
import ai.realworld.service.dto.AlBetonamuRelationDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlBetonamuRelation} and its DTO {@link AlBetonamuRelationDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlBetonamuRelationMapper extends EntityMapper<AlBetonamuRelationDTO, AlBetonamuRelation> {
    @Mapping(target = "supplier", source = "supplier", qualifiedByName = "alAlexTypeId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "alAlexTypeId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlBetonamuRelationDTO toDto(AlBetonamuRelation s);

    @Named("alAlexTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlAlexTypeDTO toDtoAlAlexTypeId(AlAlexType alAlexType);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
