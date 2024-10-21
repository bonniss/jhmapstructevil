package ai.realworld.service.mapper;

import ai.realworld.domain.AlVueVueVi;
import ai.realworld.domain.AlVueVueViUsage;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.service.dto.AlVueVueViDTO;
import ai.realworld.service.dto.AlVueVueViUsageDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.MetaverseDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlVueVueVi} and its DTO {@link AlVueVueViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlVueVueViMapper extends EntityMapper<AlVueVueViDTO, AlVueVueVi> {
    @Mapping(target = "image", source = "image", qualifiedByName = "metaverseId")
    @Mapping(target = "alVueVueViUsage", source = "alVueVueViUsage", qualifiedByName = "alVueVueViUsageId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlVueVueViDTO toDto(AlVueVueVi s);

    @Named("metaverseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MetaverseDTO toDtoMetaverseId(Metaverse metaverse);

    @Named("alVueVueViUsageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlVueVueViUsageDTO toDtoAlVueVueViUsageId(AlVueVueViUsage alVueVueViUsage);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
