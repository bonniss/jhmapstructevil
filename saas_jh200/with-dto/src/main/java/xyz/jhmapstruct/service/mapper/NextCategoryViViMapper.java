package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCategoryViVi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCategoryViViDTO;

/**
 * Mapper for the entity {@link NextCategoryViVi} and its DTO {@link NextCategoryViViDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextCategoryViViMapper extends EntityMapper<NextCategoryViViDTO, NextCategoryViVi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextCategoryViViDTO toDto(NextCategoryViVi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
