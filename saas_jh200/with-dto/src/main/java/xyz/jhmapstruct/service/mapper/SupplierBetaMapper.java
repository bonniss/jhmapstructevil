package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.ProductBeta;
import xyz.jhmapstruct.domain.SupplierBeta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.ProductBetaDTO;
import xyz.jhmapstruct.service.dto.SupplierBetaDTO;

/**
 * Mapper for the entity {@link SupplierBeta} and its DTO {@link SupplierBetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface SupplierBetaMapper extends EntityMapper<SupplierBetaDTO, SupplierBeta> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "products", source = "products", qualifiedByName = "productBetaNameSet")
    SupplierBetaDTO toDto(SupplierBeta s);

    @Mapping(target = "removeProducts", ignore = true)
    SupplierBeta toEntity(SupplierBetaDTO supplierBetaDTO);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("productBetaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductBetaDTO toDtoProductBetaName(ProductBeta productBeta);

    @Named("productBetaNameSet")
    default Set<ProductBetaDTO> toDtoProductBetaNameSet(Set<ProductBeta> productBeta) {
        return productBeta.stream().map(this::toDtoProductBetaName).collect(Collectors.toSet());
    }
}
