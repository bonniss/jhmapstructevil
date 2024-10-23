package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.CategoryGamma;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderGamma;
import xyz.jhmapstruct.domain.ProductGamma;
import xyz.jhmapstruct.domain.SupplierGamma;
import xyz.jhmapstruct.service.dto.CategoryGammaDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.OrderGammaDTO;
import xyz.jhmapstruct.service.dto.ProductGammaDTO;
import xyz.jhmapstruct.service.dto.SupplierGammaDTO;

/**
 * Mapper for the entity {@link ProductGamma} and its DTO {@link ProductGammaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductGammaMapper extends EntityMapper<ProductGammaDTO, ProductGamma> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryGammaName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "order", source = "order", qualifiedByName = "orderGammaId")
    @Mapping(target = "suppliers", source = "suppliers", qualifiedByName = "supplierGammaIdSet")
    ProductGammaDTO toDto(ProductGamma s);

    @Mapping(target = "suppliers", ignore = true)
    @Mapping(target = "removeSuppliers", ignore = true)
    ProductGamma toEntity(ProductGammaDTO productGammaDTO);

    @Named("categoryGammaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CategoryGammaDTO toDtoCategoryGammaName(CategoryGamma categoryGamma);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("orderGammaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderGammaDTO toDtoOrderGammaId(OrderGamma orderGamma);

    @Named("supplierGammaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SupplierGammaDTO toDtoSupplierGammaId(SupplierGamma supplierGamma);

    @Named("supplierGammaIdSet")
    default Set<SupplierGammaDTO> toDtoSupplierGammaIdSet(Set<SupplierGamma> supplierGamma) {
        return supplierGamma.stream().map(this::toDtoSupplierGammaId).collect(Collectors.toSet());
    }
}
