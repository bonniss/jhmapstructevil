package ai.realworld.service.mapper;

import ai.realworld.domain.AlPounder;
import ai.realworld.domain.AlPowerShell;
import ai.realworld.domain.AlProPro;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlPounderDTO;
import ai.realworld.service.dto.AlPowerShellDTO;
import ai.realworld.service.dto.AlProProDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlPowerShell} and its DTO {@link AlPowerShellDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlPowerShellMapper extends EntityMapper<AlPowerShellDTO, AlPowerShell> {
    @Mapping(target = "propertyProfile", source = "propertyProfile", qualifiedByName = "alProProId")
    @Mapping(target = "attributeTerm", source = "attributeTerm", qualifiedByName = "alPounderId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlPowerShellDTO toDto(AlPowerShell s);

    @Named("alProProId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlProProDTO toDtoAlProProId(AlProPro alProPro);

    @Named("alPounderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlPounderDTO toDtoAlPounderId(AlPounder alPounder);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
