package ai.realworld.service.mapper;

import ai.realworld.domain.AlPounderVi;
import ai.realworld.domain.AlPowerShellVi;
import ai.realworld.domain.AlProProVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlPounderViDTO;
import ai.realworld.service.dto.AlPowerShellViDTO;
import ai.realworld.service.dto.AlProProViDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlPowerShellVi} and its DTO {@link AlPowerShellViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlPowerShellViMapper extends EntityMapper<AlPowerShellViDTO, AlPowerShellVi> {
    @Mapping(target = "propertyProfile", source = "propertyProfile", qualifiedByName = "alProProViId")
    @Mapping(target = "attributeTerm", source = "attributeTerm", qualifiedByName = "alPounderViId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlPowerShellViDTO toDto(AlPowerShellVi s);

    @Named("alProProViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlProProViDTO toDtoAlProProViId(AlProProVi alProProVi);

    @Named("alPounderViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlPounderViDTO toDtoAlPounderViId(AlPounderVi alPounderVi);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
