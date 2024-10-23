package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextEmployeeVi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextEmployeeViDTO;

/**
 * Mapper for the entity {@link NextEmployeeVi} and its DTO {@link NextEmployeeViDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextEmployeeViMapper extends EntityMapper<NextEmployeeViDTO, NextEmployeeVi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextEmployeeViDTO toDto(NextEmployeeVi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
