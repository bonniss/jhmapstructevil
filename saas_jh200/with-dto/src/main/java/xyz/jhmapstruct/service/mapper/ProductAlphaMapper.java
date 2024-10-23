package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.CategoryAlpha;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderAlpha;
import xyz.jhmapstruct.domain.ProductAlpha;
import xyz.jhmapstruct.domain.SupplierAlpha;
import xyz.jhmapstruct.service.dto.CategoryAlphaDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.OrderAlphaDTO;
import xyz.jhmapstruct.service.dto.ProductAlphaDTO;
import xyz.jhmapstruct.service.dto.SupplierAlphaDTO;

/**
 * Mapper for the entity {@link ProductAlpha} and its DTO {@link ProductAlphaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductAlphaMapper extends EntityMapper<ProductAlphaDTO, ProductAlpha> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryAlphaName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "order", source = "order", qualifiedByName = "orderAlphaId")
    @Mapping(target = "suppliers", source = "suppliers", qualifiedByName = "supplierAlphaIdSet")
    ProductAlphaDTO toDto(ProductAlpha s);

    @Mapping(target = "suppliers", ignore = true)
    @Mapping(target = "removeSuppliers", ignore = true)
    ProductAlpha toEntity(ProductAlphaDTO productAlphaDTO);

    @Named("categoryAlphaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CategoryAlphaDTO toDtoCategoryAlphaName(CategoryAlpha categoryAlpha);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("orderAlphaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderAlphaDTO toDtoOrderAlphaId(OrderAlpha orderAlpha);

    @Named("supplierAlphaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SupplierAlphaDTO toDtoSupplierAlphaId(SupplierAlpha supplierAlpha);

    @Named("supplierAlphaIdSet")
    default Set<SupplierAlphaDTO> toDtoSupplierAlphaIdSet(Set<SupplierAlpha> supplierAlpha) {
        return supplierAlpha.stream().map(this::toDtoSupplierAlphaId).collect(Collectors.toSet());
    }
}
