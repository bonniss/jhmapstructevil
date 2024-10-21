package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.ProductViVi;
import xyz.jhmapstruct.domain.SupplierViVi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.ProductViViDTO;
import xyz.jhmapstruct.service.dto.SupplierViViDTO;

/**
 * Mapper for the entity {@link SupplierViVi} and its DTO {@link SupplierViViDTO}.
 */
@Mapper(componentModel = "spring")
public interface SupplierViViMapper extends EntityMapper<SupplierViViDTO, SupplierViVi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "products", source = "products", qualifiedByName = "productViViNameSet")
    SupplierViViDTO toDto(SupplierViVi s);

    @Mapping(target = "removeProducts", ignore = true)
    SupplierViVi toEntity(SupplierViViDTO supplierViViDTO);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("productViViName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductViViDTO toDtoProductViViName(ProductViVi productViVi);

    @Named("productViViNameSet")
    default Set<ProductViViDTO> toDtoProductViViNameSet(Set<ProductViVi> productViVi) {
        return productViVi.stream().map(this::toDtoProductViViName).collect(Collectors.toSet());
    }
}
