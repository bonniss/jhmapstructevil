package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.CategoryVi;
import xyz.jhmapstruct.domain.OrderVi;
import xyz.jhmapstruct.domain.ProductVi;
import xyz.jhmapstruct.domain.SupplierVi;
import xyz.jhmapstruct.service.dto.CategoryViDTO;
import xyz.jhmapstruct.service.dto.OrderViDTO;
import xyz.jhmapstruct.service.dto.ProductViDTO;
import xyz.jhmapstruct.service.dto.SupplierViDTO;

/**
 * Mapper for the entity {@link ProductVi} and its DTO {@link ProductViDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductViMapper extends EntityMapper<ProductViDTO, ProductVi> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryViName")
    @Mapping(target = "order", source = "order", qualifiedByName = "orderViId")
    @Mapping(target = "suppliers", source = "suppliers", qualifiedByName = "supplierViIdSet")
    ProductViDTO toDto(ProductVi s);

    @Mapping(target = "suppliers", ignore = true)
    @Mapping(target = "removeSuppliers", ignore = true)
    ProductVi toEntity(ProductViDTO productViDTO);

    @Named("categoryViName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CategoryViDTO toDtoCategoryViName(CategoryVi categoryVi);

    @Named("orderViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderViDTO toDtoOrderViId(OrderVi orderVi);

    @Named("supplierViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SupplierViDTO toDtoSupplierViId(SupplierVi supplierVi);

    @Named("supplierViIdSet")
    default Set<SupplierViDTO> toDtoSupplierViIdSet(Set<SupplierVi> supplierVi) {
        return supplierVi.stream().map(this::toDtoSupplierViId).collect(Collectors.toSet());
    }
}
