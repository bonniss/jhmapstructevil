package ai.realworld.service.mapper;

import ai.realworld.domain.AlCatalinaVi;
import ai.realworld.domain.Metaverse;
import ai.realworld.service.dto.AlCatalinaViDTO;
import ai.realworld.service.dto.MetaverseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlCatalinaVi} and its DTO {@link AlCatalinaViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlCatalinaViMapper extends EntityMapper<AlCatalinaViDTO, AlCatalinaVi> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "alCatalinaViId")
    @Mapping(target = "avatar", source = "avatar", qualifiedByName = "metaverseId")
    AlCatalinaViDTO toDto(AlCatalinaVi s);

    @Named("alCatalinaViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlCatalinaViDTO toDtoAlCatalinaViId(AlCatalinaVi alCatalinaVi);

    @Named("metaverseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MetaverseDTO toDtoMetaverseId(Metaverse metaverse);
}
