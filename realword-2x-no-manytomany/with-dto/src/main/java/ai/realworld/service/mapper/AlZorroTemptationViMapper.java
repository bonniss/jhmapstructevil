package ai.realworld.service.mapper;

import ai.realworld.domain.AlZorroTemptationVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.service.dto.AlZorroTemptationViDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.MetaverseDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlZorroTemptationVi} and its DTO {@link AlZorroTemptationViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlZorroTemptationViMapper extends EntityMapper<AlZorroTemptationViDTO, AlZorroTemptationVi> {
    @Mapping(target = "thumbnail", source = "thumbnail", qualifiedByName = "metaverseId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlZorroTemptationViDTO toDto(AlZorroTemptationVi s);

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
