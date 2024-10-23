package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCategoryMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCategoryMiDTO;

/**
 * Mapper for the entity {@link NextCategoryMi} and its DTO {@link NextCategoryMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextCategoryMiMapper extends EntityMapper<NextCategoryMiDTO, NextCategoryMi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextCategoryMiDTO toDto(NextCategoryMi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
