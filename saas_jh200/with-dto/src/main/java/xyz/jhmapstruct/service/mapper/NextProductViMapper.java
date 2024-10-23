package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCategoryVi;
import xyz.jhmapstruct.domain.NextOrderVi;
import xyz.jhmapstruct.domain.NextProductVi;
import xyz.jhmapstruct.domain.NextSupplierVi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCategoryViDTO;
import xyz.jhmapstruct.service.dto.NextOrderViDTO;
import xyz.jhmapstruct.service.dto.NextProductViDTO;
import xyz.jhmapstruct.service.dto.NextSupplierViDTO;

/**
 * Mapper for the entity {@link NextProductVi} and its DTO {@link NextProductViDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextProductViMapper extends EntityMapper<NextProductViDTO, NextProductVi> {
    @Mapping(target = "category", source = "category", qualifiedByName = "nextCategoryViName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "order", source = "order", qualifiedByName = "nextOrderViId")
    @Mapping(target = "suppliers", source = "suppliers", qualifiedByName = "nextSupplierViIdSet")
    NextProductViDTO toDto(NextProductVi s);

    @Mapping(target = "suppliers", ignore = true)
    @Mapping(target = "removeSuppliers", ignore = true)
    NextProductVi toEntity(NextProductViDTO nextProductViDTO);

    @Named("nextCategoryViName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextCategoryViDTO toDtoNextCategoryViName(NextCategoryVi nextCategoryVi);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextOrderViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextOrderViDTO toDtoNextOrderViId(NextOrderVi nextOrderVi);

    @Named("nextSupplierViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextSupplierViDTO toDtoNextSupplierViId(NextSupplierVi nextSupplierVi);

    @Named("nextSupplierViIdSet")
    default Set<NextSupplierViDTO> toDtoNextSupplierViIdSet(Set<NextSupplierVi> nextSupplierVi) {
        return nextSupplierVi.stream().map(this::toDtoNextSupplierViId).collect(Collectors.toSet());
    }
}
