package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.ProductVi;
import xyz.jhmapstruct.domain.SupplierVi;
import xyz.jhmapstruct.service.dto.ProductViDTO;
import xyz.jhmapstruct.service.dto.SupplierViDTO;

/**
 * Mapper for the entity {@link SupplierVi} and its DTO {@link SupplierViDTO}.
 */
@Mapper(componentModel = "spring")
public interface SupplierViMapper extends EntityMapper<SupplierViDTO, SupplierVi> {
    @Mapping(target = "products", source = "products", qualifiedByName = "productViNameSet")
    SupplierViDTO toDto(SupplierVi s);

    @Mapping(target = "removeProducts", ignore = true)
    SupplierVi toEntity(SupplierViDTO supplierViDTO);

    @Named("productViName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductViDTO toDtoProductViName(ProductVi productVi);

    @Named("productViNameSet")
    default Set<ProductViDTO> toDtoProductViNameSet(Set<ProductVi> productVi) {
        return productVi.stream().map(this::toDtoProductViName).collect(Collectors.toSet());
    }
}
