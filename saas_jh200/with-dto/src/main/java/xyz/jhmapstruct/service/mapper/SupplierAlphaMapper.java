package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.ProductAlpha;
import xyz.jhmapstruct.domain.SupplierAlpha;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.ProductAlphaDTO;
import xyz.jhmapstruct.service.dto.SupplierAlphaDTO;

/**
 * Mapper for the entity {@link SupplierAlpha} and its DTO {@link SupplierAlphaDTO}.
 */
@Mapper(componentModel = "spring")
public interface SupplierAlphaMapper extends EntityMapper<SupplierAlphaDTO, SupplierAlpha> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "products", source = "products", qualifiedByName = "productAlphaNameSet")
    SupplierAlphaDTO toDto(SupplierAlpha s);

    @Mapping(target = "removeProducts", ignore = true)
    SupplierAlpha toEntity(SupplierAlphaDTO supplierAlphaDTO);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("productAlphaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductAlphaDTO toDtoProductAlphaName(ProductAlpha productAlpha);

    @Named("productAlphaNameSet")
    default Set<ProductAlphaDTO> toDtoProductAlphaNameSet(Set<ProductAlpha> productAlpha) {
        return productAlpha.stream().map(this::toDtoProductAlphaName).collect(Collectors.toSet());
    }
}
