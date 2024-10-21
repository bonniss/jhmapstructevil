package ai.realworld.service.mapper;

import ai.realworld.domain.AlAlexTypeVi;
import ai.realworld.domain.AlAppleVi;
import ai.realworld.domain.AndreiRightHandVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.service.dto.AlAlexTypeViDTO;
import ai.realworld.service.dto.AlAppleViDTO;
import ai.realworld.service.dto.AndreiRightHandViDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.MetaverseDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlAppleVi} and its DTO {@link AlAppleViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlAppleViMapper extends EntityMapper<AlAppleViDTO, AlAppleVi> {
    @Mapping(target = "address", source = "address", qualifiedByName = "andreiRightHandViId")
    @Mapping(target = "agencyType", source = "agencyType", qualifiedByName = "alAlexTypeViId")
    @Mapping(target = "logo", source = "logo", qualifiedByName = "metaverseId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlAppleViDTO toDto(AlAppleVi s);

    @Named("andreiRightHandViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AndreiRightHandViDTO toDtoAndreiRightHandViId(AndreiRightHandVi andreiRightHandVi);

    @Named("alAlexTypeViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlAlexTypeViDTO toDtoAlAlexTypeViId(AlAlexTypeVi alAlexTypeVi);

    @Named("metaverseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MetaverseDTO toDtoMetaverseId(Metaverse metaverse);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
