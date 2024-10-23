package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCategoryMiMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCategoryMiMiDTO;

/**
 * Mapper for the entity {@link NextCategoryMiMi} and its DTO {@link NextCategoryMiMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextCategoryMiMiMapper extends EntityMapper<NextCategoryMiMiDTO, NextCategoryMiMi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextCategoryMiMiDTO toDto(NextCategoryMiMi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
