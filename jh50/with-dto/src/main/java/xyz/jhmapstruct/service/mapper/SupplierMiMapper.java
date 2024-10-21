package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.ProductMi;
import xyz.jhmapstruct.domain.SupplierMi;
import xyz.jhmapstruct.service.dto.ProductMiDTO;
import xyz.jhmapstruct.service.dto.SupplierMiDTO;

/**
 * Mapper for the entity {@link SupplierMi} and its DTO {@link SupplierMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface SupplierMiMapper extends EntityMapper<SupplierMiDTO, SupplierMi> {
    @Mapping(target = "products", source = "products", qualifiedByName = "productMiNameSet")
    SupplierMiDTO toDto(SupplierMi s);

    @Mapping(target = "removeProducts", ignore = true)
    SupplierMi toEntity(SupplierMiDTO supplierMiDTO);

    @Named("productMiName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductMiDTO toDtoProductMiName(ProductMi productMi);

    @Named("productMiNameSet")
    default Set<ProductMiDTO> toDtoProductMiNameSet(Set<ProductMi> productMi) {
        return productMi.stream().map(this::toDtoProductMiName).collect(Collectors.toSet());
    }
}
