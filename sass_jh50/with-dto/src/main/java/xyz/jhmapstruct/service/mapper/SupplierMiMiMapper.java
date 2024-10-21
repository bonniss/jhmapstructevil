package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.ProductMiMi;
import xyz.jhmapstruct.domain.SupplierMiMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.ProductMiMiDTO;
import xyz.jhmapstruct.service.dto.SupplierMiMiDTO;

/**
 * Mapper for the entity {@link SupplierMiMi} and its DTO {@link SupplierMiMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface SupplierMiMiMapper extends EntityMapper<SupplierMiMiDTO, SupplierMiMi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "products", source = "products", qualifiedByName = "productMiMiNameSet")
    SupplierMiMiDTO toDto(SupplierMiMi s);

    @Mapping(target = "removeProducts", ignore = true)
    SupplierMiMi toEntity(SupplierMiMiDTO supplierMiMiDTO);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("productMiMiName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductMiMiDTO toDtoProductMiMiName(ProductMiMi productMiMi);

    @Named("productMiMiNameSet")
    default Set<ProductMiMiDTO> toDtoProductMiMiNameSet(Set<ProductMiMi> productMiMi) {
        return productMiMi.stream().map(this::toDtoProductMiMiName).collect(Collectors.toSet());
    }
}
