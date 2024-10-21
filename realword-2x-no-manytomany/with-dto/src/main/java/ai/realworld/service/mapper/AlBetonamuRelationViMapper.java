package ai.realworld.service.mapper;

import ai.realworld.domain.AlAlexTypeVi;
import ai.realworld.domain.AlBetonamuRelationVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlAlexTypeViDTO;
import ai.realworld.service.dto.AlBetonamuRelationViDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlBetonamuRelationVi} and its DTO {@link AlBetonamuRelationViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlBetonamuRelationViMapper extends EntityMapper<AlBetonamuRelationViDTO, AlBetonamuRelationVi> {
    @Mapping(target = "supplier", source = "supplier", qualifiedByName = "alAlexTypeViId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "alAlexTypeViId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlBetonamuRelationViDTO toDto(AlBetonamuRelationVi s);

    @Named("alAlexTypeViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlAlexTypeViDTO toDtoAlAlexTypeViId(AlAlexTypeVi alAlexTypeVi);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
