package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCategoryViVi;
import xyz.jhmapstruct.domain.NextOrderViVi;
import xyz.jhmapstruct.domain.NextProductViVi;
import xyz.jhmapstruct.domain.NextSupplierViVi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCategoryViViDTO;
import xyz.jhmapstruct.service.dto.NextOrderViViDTO;
import xyz.jhmapstruct.service.dto.NextProductViViDTO;
import xyz.jhmapstruct.service.dto.NextSupplierViViDTO;

/**
 * Mapper for the entity {@link NextProductViVi} and its DTO {@link NextProductViViDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextProductViViMapper extends EntityMapper<NextProductViViDTO, NextProductViVi> {
    @Mapping(target = "category", source = "category", qualifiedByName = "nextCategoryViViName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "order", source = "order", qualifiedByName = "nextOrderViViId")
    @Mapping(target = "suppliers", source = "suppliers", qualifiedByName = "nextSupplierViViIdSet")
    NextProductViViDTO toDto(NextProductViVi s);

    @Mapping(target = "suppliers", ignore = true)
    @Mapping(target = "removeSuppliers", ignore = true)
    NextProductViVi toEntity(NextProductViViDTO nextProductViViDTO);

    @Named("nextCategoryViViName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextCategoryViViDTO toDtoNextCategoryViViName(NextCategoryViVi nextCategoryViVi);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextOrderViViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextOrderViViDTO toDtoNextOrderViViId(NextOrderViVi nextOrderViVi);

    @Named("nextSupplierViViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextSupplierViViDTO toDtoNextSupplierViViId(NextSupplierViVi nextSupplierViVi);

    @Named("nextSupplierViViIdSet")
    default Set<NextSupplierViViDTO> toDtoNextSupplierViViIdSet(Set<NextSupplierViVi> nextSupplierViVi) {
        return nextSupplierViVi.stream().map(this::toDtoNextSupplierViViId).collect(Collectors.toSet());
    }
}
