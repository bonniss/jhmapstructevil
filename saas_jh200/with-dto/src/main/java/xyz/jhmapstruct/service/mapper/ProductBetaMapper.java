package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.CategoryBeta;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderBeta;
import xyz.jhmapstruct.domain.ProductBeta;
import xyz.jhmapstruct.domain.SupplierBeta;
import xyz.jhmapstruct.service.dto.CategoryBetaDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.OrderBetaDTO;
import xyz.jhmapstruct.service.dto.ProductBetaDTO;
import xyz.jhmapstruct.service.dto.SupplierBetaDTO;

/**
 * Mapper for the entity {@link ProductBeta} and its DTO {@link ProductBetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductBetaMapper extends EntityMapper<ProductBetaDTO, ProductBeta> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryBetaName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "order", source = "order", qualifiedByName = "orderBetaId")
    @Mapping(target = "suppliers", source = "suppliers", qualifiedByName = "supplierBetaIdSet")
    ProductBetaDTO toDto(ProductBeta s);

    @Mapping(target = "suppliers", ignore = true)
    @Mapping(target = "removeSuppliers", ignore = true)
    ProductBeta toEntity(ProductBetaDTO productBetaDTO);

    @Named("categoryBetaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CategoryBetaDTO toDtoCategoryBetaName(CategoryBeta categoryBeta);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("orderBetaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderBetaDTO toDtoOrderBetaId(OrderBeta orderBeta);

    @Named("supplierBetaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SupplierBetaDTO toDtoSupplierBetaId(SupplierBeta supplierBeta);

    @Named("supplierBetaIdSet")
    default Set<SupplierBetaDTO> toDtoSupplierBetaIdSet(Set<SupplierBeta> supplierBeta) {
        return supplierBeta.stream().map(this::toDtoSupplierBetaId).collect(Collectors.toSet());
    }
}
