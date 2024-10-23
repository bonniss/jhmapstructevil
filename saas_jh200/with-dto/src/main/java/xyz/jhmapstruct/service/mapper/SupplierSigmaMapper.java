package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.ProductSigma;
import xyz.jhmapstruct.domain.SupplierSigma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.ProductSigmaDTO;
import xyz.jhmapstruct.service.dto.SupplierSigmaDTO;

/**
 * Mapper for the entity {@link SupplierSigma} and its DTO {@link SupplierSigmaDTO}.
 */
@Mapper(componentModel = "spring")
public interface SupplierSigmaMapper extends EntityMapper<SupplierSigmaDTO, SupplierSigma> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "products", source = "products", qualifiedByName = "productSigmaNameSet")
    SupplierSigmaDTO toDto(SupplierSigma s);

    @Mapping(target = "removeProducts", ignore = true)
    SupplierSigma toEntity(SupplierSigmaDTO supplierSigmaDTO);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("productSigmaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductSigmaDTO toDtoProductSigmaName(ProductSigma productSigma);

    @Named("productSigmaNameSet")
    default Set<ProductSigmaDTO> toDtoProductSigmaNameSet(Set<ProductSigma> productSigma) {
        return productSigma.stream().map(this::toDtoProductSigmaName).collect(Collectors.toSet());
    }
}
