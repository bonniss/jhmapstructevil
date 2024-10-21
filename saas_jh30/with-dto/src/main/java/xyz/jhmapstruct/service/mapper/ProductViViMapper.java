package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.CategoryViVi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderViVi;
import xyz.jhmapstruct.domain.ProductViVi;
import xyz.jhmapstruct.domain.SupplierViVi;
import xyz.jhmapstruct.service.dto.CategoryViViDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.OrderViViDTO;
import xyz.jhmapstruct.service.dto.ProductViViDTO;
import xyz.jhmapstruct.service.dto.SupplierViViDTO;

/**
 * Mapper for the entity {@link ProductViVi} and its DTO {@link ProductViViDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductViViMapper extends EntityMapper<ProductViViDTO, ProductViVi> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryViViName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "order", source = "order", qualifiedByName = "orderViViId")
    @Mapping(target = "suppliers", source = "suppliers", qualifiedByName = "supplierViViIdSet")
    ProductViViDTO toDto(ProductViVi s);

    @Mapping(target = "suppliers", ignore = true)
    @Mapping(target = "removeSuppliers", ignore = true)
    ProductViVi toEntity(ProductViViDTO productViViDTO);

    @Named("categoryViViName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CategoryViViDTO toDtoCategoryViViName(CategoryViVi categoryViVi);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("orderViViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderViViDTO toDtoOrderViViId(OrderViVi orderViVi);

    @Named("supplierViViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SupplierViViDTO toDtoSupplierViViId(SupplierViVi supplierViVi);

    @Named("supplierViViIdSet")
    default Set<SupplierViViDTO> toDtoSupplierViViIdSet(Set<SupplierViVi> supplierViVi) {
        return supplierViVi.stream().map(this::toDtoSupplierViViId).collect(Collectors.toSet());
    }
}
