package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCategoryMi;
import xyz.jhmapstruct.domain.NextSupplierMi;
import xyz.jhmapstruct.domain.OrderMi;
import xyz.jhmapstruct.domain.ProductMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCategoryMiDTO;
import xyz.jhmapstruct.service.dto.NextSupplierMiDTO;
import xyz.jhmapstruct.service.dto.OrderMiDTO;
import xyz.jhmapstruct.service.dto.ProductMiDTO;

/**
 * Mapper for the entity {@link ProductMi} and its DTO {@link ProductMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMiMapper extends EntityMapper<ProductMiDTO, ProductMi> {
    @Mapping(target = "category", source = "category", qualifiedByName = "nextCategoryMiName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "order", source = "order", qualifiedByName = "orderMiId")
    @Mapping(target = "suppliers", source = "suppliers", qualifiedByName = "nextSupplierMiIdSet")
    ProductMiDTO toDto(ProductMi s);

    @Mapping(target = "suppliers", ignore = true)
    @Mapping(target = "removeSuppliers", ignore = true)
    ProductMi toEntity(ProductMiDTO productMiDTO);

    @Named("nextCategoryMiName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextCategoryMiDTO toDtoNextCategoryMiName(NextCategoryMi nextCategoryMi);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("orderMiId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderMiDTO toDtoOrderMiId(OrderMi orderMi);

    @Named("nextSupplierMiId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextSupplierMiDTO toDtoNextSupplierMiId(NextSupplierMi nextSupplierMi);

    @Named("nextSupplierMiIdSet")
    default Set<NextSupplierMiDTO> toDtoNextSupplierMiIdSet(Set<NextSupplierMi> nextSupplierMi) {
        return nextSupplierMi.stream().map(this::toDtoNextSupplierMiId).collect(Collectors.toSet());
    }
}
