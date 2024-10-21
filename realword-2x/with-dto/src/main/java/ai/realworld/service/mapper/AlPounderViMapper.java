package ai.realworld.service.mapper;

import ai.realworld.domain.AlPedroTaxVi;
import ai.realworld.domain.AlPounderVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlPedroTaxViDTO;
import ai.realworld.service.dto.AlPounderViDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlPounderVi} and its DTO {@link AlPounderViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlPounderViMapper extends EntityMapper<AlPounderViDTO, AlPounderVi> {
    @Mapping(target = "attributeTaxonomy", source = "attributeTaxonomy", qualifiedByName = "alPedroTaxViId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlPounderViDTO toDto(AlPounderVi s);

    @Named("alPedroTaxViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlPedroTaxViDTO toDtoAlPedroTaxViId(AlPedroTaxVi alPedroTaxVi);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
