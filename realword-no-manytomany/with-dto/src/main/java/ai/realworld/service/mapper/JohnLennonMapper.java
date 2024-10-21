package ai.realworld.service.mapper;

import ai.realworld.domain.Initium;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.domain.OlAlmantinoMilo;
import ai.realworld.domain.OlMaster;
import ai.realworld.service.dto.InitiumDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.MetaverseDTO;
import ai.realworld.service.dto.OlAlmantinoMiloDTO;
import ai.realworld.service.dto.OlMasterDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link JohnLennon} and its DTO {@link JohnLennonDTO}.
 */
@Mapper(componentModel = "spring")
public interface JohnLennonMapper extends EntityMapper<JohnLennonDTO, JohnLennon> {
    @Mapping(target = "logo", source = "logo", qualifiedByName = "metaverseId")
    @Mapping(target = "appManager", source = "appManager", qualifiedByName = "olAlmantinoMiloId")
    @Mapping(target = "organization", source = "organization", qualifiedByName = "olMasterId")
    @Mapping(target = "jelloInitium", source = "jelloInitium", qualifiedByName = "initiumId")
    @Mapping(target = "inhouseInitium", source = "inhouseInitium", qualifiedByName = "initiumId")
    JohnLennonDTO toDto(JohnLennon s);

    @Named("metaverseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MetaverseDTO toDtoMetaverseId(Metaverse metaverse);

    @Named("olAlmantinoMiloId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OlAlmantinoMiloDTO toDtoOlAlmantinoMiloId(OlAlmantinoMilo olAlmantinoMilo);

    @Named("olMasterId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OlMasterDTO toDtoOlMasterId(OlMaster olMaster);

    @Named("initiumId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InitiumDTO toDtoInitiumId(Initium initium);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
