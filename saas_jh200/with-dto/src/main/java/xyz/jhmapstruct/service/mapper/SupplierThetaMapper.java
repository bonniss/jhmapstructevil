package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.ProductTheta;
import xyz.jhmapstruct.domain.SupplierTheta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.ProductThetaDTO;
import xyz.jhmapstruct.service.dto.SupplierThetaDTO;

/**
 * Mapper for the entity {@link SupplierTheta} and its DTO {@link SupplierThetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface SupplierThetaMapper extends EntityMapper<SupplierThetaDTO, SupplierTheta> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "products", source = "products", qualifiedByName = "productThetaNameSet")
    SupplierThetaDTO toDto(SupplierTheta s);

    @Mapping(target = "removeProducts", ignore = true)
    SupplierTheta toEntity(SupplierThetaDTO supplierThetaDTO);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("productThetaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductThetaDTO toDtoProductThetaName(ProductTheta productTheta);

    @Named("productThetaNameSet")
    default Set<ProductThetaDTO> toDtoProductThetaNameSet(Set<ProductTheta> productTheta) {
        return productTheta.stream().map(this::toDtoProductThetaName).collect(Collectors.toSet());
    }
}
