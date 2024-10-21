package ai.realworld.service.mapper;

import ai.realworld.domain.AlMemTier;
import ai.realworld.domain.AlPacino;
import ai.realworld.domain.AlVueVueUsage;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlMemTierDTO;
import ai.realworld.service.dto.AlPacinoDTO;
import ai.realworld.service.dto.AlVueVueUsageDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlPacino} and its DTO {@link AlPacinoDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlPacinoMapper extends EntityMapper<AlPacinoDTO, AlPacino> {
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    @Mapping(target = "membershipTier", source = "membershipTier", qualifiedByName = "alMemTierId")
    @Mapping(target = "alVueVueUsage", source = "alVueVueUsage", qualifiedByName = "alVueVueUsageId")
    AlPacinoDTO toDto(AlPacino s);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    @Named("alMemTierId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlMemTierDTO toDtoAlMemTierId(AlMemTier alMemTier);

    @Named("alVueVueUsageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlVueVueUsageDTO toDtoAlVueVueUsageId(AlVueVueUsage alVueVueUsage);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
