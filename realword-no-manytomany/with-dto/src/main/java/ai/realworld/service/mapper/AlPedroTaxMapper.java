package ai.realworld.service.mapper;

import ai.realworld.domain.AlPedroTax;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlPedroTaxDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlPedroTax} and its DTO {@link AlPedroTaxDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlPedroTaxMapper extends EntityMapper<AlPedroTaxDTO, AlPedroTax> {
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlPedroTaxDTO toDto(AlPedroTax s);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
