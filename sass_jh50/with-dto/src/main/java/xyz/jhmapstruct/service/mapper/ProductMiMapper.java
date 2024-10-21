package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.CategoryMi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderMi;
import xyz.jhmapstruct.domain.ProductMi;
import xyz.jhmapstruct.domain.SupplierMi;
import xyz.jhmapstruct.service.dto.CategoryMiDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.OrderMiDTO;
import xyz.jhmapstruct.service.dto.ProductMiDTO;
import xyz.jhmapstruct.service.dto.SupplierMiDTO;

/**
 * Mapper for the entity {@link ProductMi} and its DTO {@link ProductMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMiMapper extends EntityMapper<ProductMiDTO, ProductMi> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryMiName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "order", source = "order", qualifiedByName = "orderMiId")
    @Mapping(target = "suppliers", source = "suppliers", qualifiedByName = "supplierMiIdSet")
    ProductMiDTO toDto(ProductMi s);

    @Mapping(target = "suppliers", ignore = true)
    @Mapping(target = "removeSuppliers", ignore = true)
    ProductMi toEntity(ProductMiDTO productMiDTO);

    @Named("categoryMiName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CategoryMiDTO toDtoCategoryMiName(CategoryMi categoryMi);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("orderMiId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderMiDTO toDtoOrderMiId(OrderMi orderMi);

    @Named("supplierMiId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SupplierMiDTO toDtoSupplierMiId(SupplierMi supplierMi);

    @Named("supplierMiIdSet")
    default Set<SupplierMiDTO> toDtoSupplierMiIdSet(Set<SupplierMi> supplierMi) {
        return supplierMi.stream().map(this::toDtoSupplierMiId).collect(Collectors.toSet());
    }
}
