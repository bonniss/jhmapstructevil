package ai.realworld.service.mapper;

import ai.realworld.domain.AlPedroTaxVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlPedroTaxViDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlPedroTaxVi} and its DTO {@link AlPedroTaxViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlPedroTaxViMapper extends EntityMapper<AlPedroTaxViDTO, AlPedroTaxVi> {
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlPedroTaxViDTO toDto(AlPedroTaxVi s);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
