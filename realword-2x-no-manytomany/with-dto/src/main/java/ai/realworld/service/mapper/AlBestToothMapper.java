package ai.realworld.service.mapper;

import ai.realworld.domain.AlBestTooth;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlBestToothDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlBestTooth} and its DTO {@link AlBestToothDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlBestToothMapper extends EntityMapper<AlBestToothDTO, AlBestTooth> {
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlBestToothDTO toDto(AlBestTooth s);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
