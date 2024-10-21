package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.CategoryMiMi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderMiMi;
import xyz.jhmapstruct.domain.ProductMiMi;
import xyz.jhmapstruct.domain.SupplierMiMi;
import xyz.jhmapstruct.service.dto.CategoryMiMiDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.OrderMiMiDTO;
import xyz.jhmapstruct.service.dto.ProductMiMiDTO;
import xyz.jhmapstruct.service.dto.SupplierMiMiDTO;

/**
 * Mapper for the entity {@link ProductMiMi} and its DTO {@link ProductMiMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMiMiMapper extends EntityMapper<ProductMiMiDTO, ProductMiMi> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryMiMiName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "order", source = "order", qualifiedByName = "orderMiMiId")
    @Mapping(target = "suppliers", source = "suppliers", qualifiedByName = "supplierMiMiIdSet")
    ProductMiMiDTO toDto(ProductMiMi s);

    @Mapping(target = "suppliers", ignore = true)
    @Mapping(target = "removeSuppliers", ignore = true)
    ProductMiMi toEntity(ProductMiMiDTO productMiMiDTO);

    @Named("categoryMiMiName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CategoryMiMiDTO toDtoCategoryMiMiName(CategoryMiMi categoryMiMi);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("orderMiMiId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderMiMiDTO toDtoOrderMiMiId(OrderMiMi orderMiMi);

    @Named("supplierMiMiId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SupplierMiMiDTO toDtoSupplierMiMiId(SupplierMiMi supplierMiMi);

    @Named("supplierMiMiIdSet")
    default Set<SupplierMiMiDTO> toDtoSupplierMiMiIdSet(Set<SupplierMiMi> supplierMiMi) {
        return supplierMiMi.stream().map(this::toDtoSupplierMiMiId).collect(Collectors.toSet());
    }
}
