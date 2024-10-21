package ai.realworld.service.mapper;

import ai.realworld.domain.EdSheeran;
import ai.realworld.domain.HandCraft;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Rihanna;
import ai.realworld.service.dto.EdSheeranDTO;
import ai.realworld.service.dto.HandCraftDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.RihannaDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HandCraft} and its DTO {@link HandCraftDTO}.
 */
@Mapper(componentModel = "spring")
public interface HandCraftMapper extends EntityMapper<HandCraftDTO, HandCraft> {
    @Mapping(target = "agent", source = "agent", qualifiedByName = "edSheeranId")
    @Mapping(target = "role", source = "role", qualifiedByName = "rihannaId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    HandCraftDTO toDto(HandCraft s);

    @Named("edSheeranId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EdSheeranDTO toDtoEdSheeranId(EdSheeran edSheeran);

    @Named("rihannaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RihannaDTO toDtoRihannaId(Rihanna rihanna);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
