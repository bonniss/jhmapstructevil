package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextEmployeeAlpha;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextEmployeeAlphaDTO;

/**
 * Mapper for the entity {@link NextEmployeeAlpha} and its DTO {@link NextEmployeeAlphaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextEmployeeAlphaMapper extends EntityMapper<NextEmployeeAlphaDTO, NextEmployeeAlpha> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextEmployeeAlphaDTO toDto(NextEmployeeAlpha s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
