package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCategoryAlpha;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCategoryAlphaDTO;

/**
 * Mapper for the entity {@link NextCategoryAlpha} and its DTO {@link NextCategoryAlphaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextCategoryAlphaMapper extends EntityMapper<NextCategoryAlphaDTO, NextCategoryAlpha> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextCategoryAlphaDTO toDto(NextCategoryAlpha s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
