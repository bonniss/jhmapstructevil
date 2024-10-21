package ai.realworld.service.mapper;

import ai.realworld.domain.OlAlmantinoMilo;
import ai.realworld.domain.OlMaster;
import ai.realworld.service.dto.OlAlmantinoMiloDTO;
import ai.realworld.service.dto.OlMasterDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OlAlmantinoMilo} and its DTO {@link OlAlmantinoMiloDTO}.
 */
@Mapper(componentModel = "spring")
public interface OlAlmantinoMiloMapper extends EntityMapper<OlAlmantinoMiloDTO, OlAlmantinoMilo> {
    @Mapping(target = "organization", source = "organization", qualifiedByName = "olMasterId")
    OlAlmantinoMiloDTO toDto(OlAlmantinoMilo s);

    @Named("olMasterId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OlMasterDTO toDtoOlMasterId(OlMaster olMaster);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
