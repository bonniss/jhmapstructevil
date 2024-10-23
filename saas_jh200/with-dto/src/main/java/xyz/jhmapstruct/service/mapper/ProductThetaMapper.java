package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.CategoryTheta;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderTheta;
import xyz.jhmapstruct.domain.ProductTheta;
import xyz.jhmapstruct.domain.SupplierTheta;
import xyz.jhmapstruct.service.dto.CategoryThetaDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.OrderThetaDTO;
import xyz.jhmapstruct.service.dto.ProductThetaDTO;
import xyz.jhmapstruct.service.dto.SupplierThetaDTO;

/**
 * Mapper for the entity {@link ProductTheta} and its DTO {@link ProductThetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductThetaMapper extends EntityMapper<ProductThetaDTO, ProductTheta> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryThetaName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "order", source = "order", qualifiedByName = "orderThetaId")
    @Mapping(target = "suppliers", source = "suppliers", qualifiedByName = "supplierThetaIdSet")
    ProductThetaDTO toDto(ProductTheta s);

    @Mapping(target = "suppliers", ignore = true)
    @Mapping(target = "removeSuppliers", ignore = true)
    ProductTheta toEntity(ProductThetaDTO productThetaDTO);

    @Named("categoryThetaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CategoryThetaDTO toDtoCategoryThetaName(CategoryTheta categoryTheta);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("orderThetaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderThetaDTO toDtoOrderThetaId(OrderTheta orderTheta);

    @Named("supplierThetaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SupplierThetaDTO toDtoSupplierThetaId(SupplierTheta supplierTheta);

    @Named("supplierThetaIdSet")
    default Set<SupplierThetaDTO> toDtoSupplierThetaIdSet(Set<SupplierTheta> supplierTheta) {
        return supplierTheta.stream().map(this::toDtoSupplierThetaId).collect(Collectors.toSet());
    }
}
