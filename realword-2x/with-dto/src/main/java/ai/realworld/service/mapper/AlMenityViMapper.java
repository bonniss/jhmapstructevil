package ai.realworld.service.mapper;

import ai.realworld.domain.AlMenityVi;
import ai.realworld.domain.AlProProVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlMenityViDTO;
import ai.realworld.service.dto.AlProProViDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlMenityVi} and its DTO {@link AlMenityViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlMenityViMapper extends EntityMapper<AlMenityViDTO, AlMenityVi> {
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    @Mapping(target = "propertyProfiles", source = "propertyProfiles", qualifiedByName = "alProProViIdSet")
    AlMenityViDTO toDto(AlMenityVi s);

    @Mapping(target = "propertyProfiles", ignore = true)
    @Mapping(target = "removePropertyProfile", ignore = true)
    AlMenityVi toEntity(AlMenityViDTO alMenityViDTO);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    @Named("alProProViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlProProViDTO toDtoAlProProViId(AlProProVi alProProVi);

    @Named("alProProViIdSet")
    default Set<AlProProViDTO> toDtoAlProProViIdSet(Set<AlProProVi> alProProVi) {
        return alProProVi.stream().map(this::toDtoAlProProViId).collect(Collectors.toSet());
    }

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
