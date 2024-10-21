package ai.realworld.service.mapper;

import ai.realworld.domain.AllMassageThai;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.service.dto.AllMassageThaiDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.MetaverseDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AllMassageThai} and its DTO {@link AllMassageThaiDTO}.
 */
@Mapper(componentModel = "spring")
public interface AllMassageThaiMapper extends EntityMapper<AllMassageThaiDTO, AllMassageThai> {
    @Mapping(target = "thumbnail", source = "thumbnail", qualifiedByName = "metaverseId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AllMassageThaiDTO toDto(AllMassageThai s);

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
