package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.ProductGamma;
import xyz.jhmapstruct.domain.SupplierGamma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.ProductGammaDTO;
import xyz.jhmapstruct.service.dto.SupplierGammaDTO;

/**
 * Mapper for the entity {@link SupplierGamma} and its DTO {@link SupplierGammaDTO}.
 */
@Mapper(componentModel = "spring")
public interface SupplierGammaMapper extends EntityMapper<SupplierGammaDTO, SupplierGamma> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "products", source = "products", qualifiedByName = "productGammaNameSet")
    SupplierGammaDTO toDto(SupplierGamma s);

    @Mapping(target = "removeProducts", ignore = true)
    SupplierGamma toEntity(SupplierGammaDTO supplierGammaDTO);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("productGammaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductGammaDTO toDtoProductGammaName(ProductGamma productGamma);

    @Named("productGammaNameSet")
    default Set<ProductGammaDTO> toDtoProductGammaNameSet(Set<ProductGamma> productGamma) {
        return productGamma.stream().map(this::toDtoProductGammaName).collect(Collectors.toSet());
    }
}
