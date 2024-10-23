package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCategoryMiMi;
import xyz.jhmapstruct.domain.NextOrderMiMi;
import xyz.jhmapstruct.domain.NextProductMiMi;
import xyz.jhmapstruct.domain.NextSupplierMiMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCategoryMiMiDTO;
import xyz.jhmapstruct.service.dto.NextOrderMiMiDTO;
import xyz.jhmapstruct.service.dto.NextProductMiMiDTO;
import xyz.jhmapstruct.service.dto.NextSupplierMiMiDTO;

/**
 * Mapper for the entity {@link NextProductMiMi} and its DTO {@link NextProductMiMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextProductMiMiMapper extends EntityMapper<NextProductMiMiDTO, NextProductMiMi> {
    @Mapping(target = "category", source = "category", qualifiedByName = "nextCategoryMiMiName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "order", source = "order", qualifiedByName = "nextOrderMiMiId")
    @Mapping(target = "suppliers", source = "suppliers", qualifiedByName = "nextSupplierMiMiIdSet")
    NextProductMiMiDTO toDto(NextProductMiMi s);

    @Mapping(target = "suppliers", ignore = true)
    @Mapping(target = "removeSuppliers", ignore = true)
    NextProductMiMi toEntity(NextProductMiMiDTO nextProductMiMiDTO);

    @Named("nextCategoryMiMiName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextCategoryMiMiDTO toDtoNextCategoryMiMiName(NextCategoryMiMi nextCategoryMiMi);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextOrderMiMiId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextOrderMiMiDTO toDtoNextOrderMiMiId(NextOrderMiMi nextOrderMiMi);

    @Named("nextSupplierMiMiId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextSupplierMiMiDTO toDtoNextSupplierMiMiId(NextSupplierMiMi nextSupplierMiMi);

    @Named("nextSupplierMiMiIdSet")
    default Set<NextSupplierMiMiDTO> toDtoNextSupplierMiMiIdSet(Set<NextSupplierMiMi> nextSupplierMiMi) {
        return nextSupplierMiMi.stream().map(this::toDtoNextSupplierMiMiId).collect(Collectors.toSet());
    }
}
