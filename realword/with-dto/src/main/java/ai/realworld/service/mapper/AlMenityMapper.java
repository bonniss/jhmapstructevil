package ai.realworld.service.mapper;

import ai.realworld.domain.AlMenity;
import ai.realworld.domain.AlProPro;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlMenityDTO;
import ai.realworld.service.dto.AlProProDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlMenity} and its DTO {@link AlMenityDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlMenityMapper extends EntityMapper<AlMenityDTO, AlMenity> {
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    @Mapping(target = "propertyProfiles", source = "propertyProfiles", qualifiedByName = "alProProIdSet")
    AlMenityDTO toDto(AlMenity s);

    @Mapping(target = "propertyProfiles", ignore = true)
    @Mapping(target = "removePropertyProfile", ignore = true)
    AlMenity toEntity(AlMenityDTO alMenityDTO);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    @Named("alProProId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlProProDTO toDtoAlProProId(AlProPro alProPro);

    @Named("alProProIdSet")
    default Set<AlProProDTO> toDtoAlProProIdSet(Set<AlProPro> alProPro) {
        return alProPro.stream().map(this::toDtoAlProProId).collect(Collectors.toSet());
    }

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
