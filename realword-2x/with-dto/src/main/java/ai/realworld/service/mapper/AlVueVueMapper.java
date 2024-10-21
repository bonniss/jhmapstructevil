package ai.realworld.service.mapper;

import ai.realworld.domain.AlVueVue;
import ai.realworld.domain.AlVueVueUsage;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.service.dto.AlVueVueDTO;
import ai.realworld.service.dto.AlVueVueUsageDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.MetaverseDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlVueVue} and its DTO {@link AlVueVueDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlVueVueMapper extends EntityMapper<AlVueVueDTO, AlVueVue> {
    @Mapping(target = "image", source = "image", qualifiedByName = "metaverseId")
    @Mapping(target = "alVueVueUsage", source = "alVueVueUsage", qualifiedByName = "alVueVueUsageId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlVueVueDTO toDto(AlVueVue s);

    @Named("metaverseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MetaverseDTO toDtoMetaverseId(Metaverse metaverse);

    @Named("alVueVueUsageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlVueVueUsageDTO toDtoAlVueVueUsageId(AlVueVueUsage alVueVueUsage);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
