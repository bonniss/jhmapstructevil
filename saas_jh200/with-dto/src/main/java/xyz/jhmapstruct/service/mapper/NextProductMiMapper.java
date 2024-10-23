package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.CategoryMi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextOrderMi;
import xyz.jhmapstruct.domain.NextProductMi;
import xyz.jhmapstruct.domain.SupplierMi;
import xyz.jhmapstruct.service.dto.CategoryMiDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextOrderMiDTO;
import xyz.jhmapstruct.service.dto.NextProductMiDTO;
import xyz.jhmapstruct.service.dto.SupplierMiDTO;

/**
 * Mapper for the entity {@link NextProductMi} and its DTO {@link NextProductMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextProductMiMapper extends EntityMapper<NextProductMiDTO, NextProductMi> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryMiName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "order", source = "order", qualifiedByName = "nextOrderMiId")
    @Mapping(target = "suppliers", source = "suppliers", qualifiedByName = "supplierMiIdSet")
    NextProductMiDTO toDto(NextProductMi s);

    @Mapping(target = "suppliers", ignore = true)
    @Mapping(target = "removeSuppliers", ignore = true)
    NextProductMi toEntity(NextProductMiDTO nextProductMiDTO);

    @Named("categoryMiName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CategoryMiDTO toDtoCategoryMiName(CategoryMi categoryMi);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextOrderMiId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextOrderMiDTO toDtoNextOrderMiId(NextOrderMi nextOrderMi);

    @Named("supplierMiId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SupplierMiDTO toDtoSupplierMiId(SupplierMi supplierMi);

    @Named("supplierMiIdSet")
    default Set<SupplierMiDTO> toDtoSupplierMiIdSet(Set<SupplierMi> supplierMi) {
        return supplierMi.stream().map(this::toDtoSupplierMiId).collect(Collectors.toSet());
    }
}
