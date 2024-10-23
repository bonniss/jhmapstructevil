package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.CategorySigma;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderSigma;
import xyz.jhmapstruct.domain.ProductSigma;
import xyz.jhmapstruct.domain.SupplierSigma;
import xyz.jhmapstruct.service.dto.CategorySigmaDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.OrderSigmaDTO;
import xyz.jhmapstruct.service.dto.ProductSigmaDTO;
import xyz.jhmapstruct.service.dto.SupplierSigmaDTO;

/**
 * Mapper for the entity {@link ProductSigma} and its DTO {@link ProductSigmaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductSigmaMapper extends EntityMapper<ProductSigmaDTO, ProductSigma> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categorySigmaName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "order", source = "order", qualifiedByName = "orderSigmaId")
    @Mapping(target = "suppliers", source = "suppliers", qualifiedByName = "supplierSigmaIdSet")
    ProductSigmaDTO toDto(ProductSigma s);

    @Mapping(target = "suppliers", ignore = true)
    @Mapping(target = "removeSuppliers", ignore = true)
    ProductSigma toEntity(ProductSigmaDTO productSigmaDTO);

    @Named("categorySigmaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CategorySigmaDTO toDtoCategorySigmaName(CategorySigma categorySigma);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("orderSigmaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderSigmaDTO toDtoOrderSigmaId(OrderSigma orderSigma);

    @Named("supplierSigmaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SupplierSigmaDTO toDtoSupplierSigmaId(SupplierSigma supplierSigma);

    @Named("supplierSigmaIdSet")
    default Set<SupplierSigmaDTO> toDtoSupplierSigmaIdSet(Set<SupplierSigma> supplierSigma) {
        return supplierSigma.stream().map(this::toDtoSupplierSigmaId).collect(Collectors.toSet());
    }
}
