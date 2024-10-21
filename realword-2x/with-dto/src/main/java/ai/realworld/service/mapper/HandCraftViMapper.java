package ai.realworld.service.mapper;

import ai.realworld.domain.EdSheeranVi;
import ai.realworld.domain.HandCraftVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.RihannaVi;
import ai.realworld.service.dto.EdSheeranViDTO;
import ai.realworld.service.dto.HandCraftViDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.RihannaViDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HandCraftVi} and its DTO {@link HandCraftViDTO}.
 */
@Mapper(componentModel = "spring")
public interface HandCraftViMapper extends EntityMapper<HandCraftViDTO, HandCraftVi> {
    @Mapping(target = "agent", source = "agent", qualifiedByName = "edSheeranViId")
    @Mapping(target = "role", source = "role", qualifiedByName = "rihannaViId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    HandCraftViDTO toDto(HandCraftVi s);

    @Named("edSheeranViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EdSheeranViDTO toDtoEdSheeranViId(EdSheeranVi edSheeranVi);

    @Named("rihannaViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RihannaViDTO toDtoRihannaViId(RihannaVi rihannaVi);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
