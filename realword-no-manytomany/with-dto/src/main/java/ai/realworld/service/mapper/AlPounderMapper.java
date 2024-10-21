package ai.realworld.service.mapper;

import ai.realworld.domain.AlPedroTax;
import ai.realworld.domain.AlPounder;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlPedroTaxDTO;
import ai.realworld.service.dto.AlPounderDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlPounder} and its DTO {@link AlPounderDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlPounderMapper extends EntityMapper<AlPounderDTO, AlPounder> {
    @Mapping(target = "attributeTaxonomy", source = "attributeTaxonomy", qualifiedByName = "alPedroTaxId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlPounderDTO toDto(AlPounder s);

    @Named("alPedroTaxId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlPedroTaxDTO toDtoAlPedroTaxId(AlPedroTax alPedroTax);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
