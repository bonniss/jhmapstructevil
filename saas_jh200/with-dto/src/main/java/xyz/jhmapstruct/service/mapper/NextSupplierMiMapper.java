package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextSupplierMi;
import xyz.jhmapstruct.domain.ProductMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextSupplierMiDTO;
import xyz.jhmapstruct.service.dto.ProductMiDTO;

/**
 * Mapper for the entity {@link NextSupplierMi} and its DTO {@link NextSupplierMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextSupplierMiMapper extends EntityMapper<NextSupplierMiDTO, NextSupplierMi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "products", source = "products", qualifiedByName = "productMiNameSet")
    NextSupplierMiDTO toDto(NextSupplierMi s);

    @Mapping(target = "removeProducts", ignore = true)
    NextSupplierMi toEntity(NextSupplierMiDTO nextSupplierMiDTO);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("productMiName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductMiDTO toDtoProductMiName(ProductMi productMi);

    @Named("productMiNameSet")
    default Set<ProductMiDTO> toDtoProductMiNameSet(Set<ProductMi> productMi) {
        return productMi.stream().map(this::toDtoProductMiName).collect(Collectors.toSet());
    }
}
