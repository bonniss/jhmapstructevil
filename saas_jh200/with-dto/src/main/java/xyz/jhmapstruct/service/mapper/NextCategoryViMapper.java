package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCategoryVi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCategoryViDTO;

/**
 * Mapper for the entity {@link NextCategoryVi} and its DTO {@link NextCategoryViDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextCategoryViMapper extends EntityMapper<NextCategoryViDTO, NextCategoryVi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextCategoryViDTO toDto(NextCategoryVi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
