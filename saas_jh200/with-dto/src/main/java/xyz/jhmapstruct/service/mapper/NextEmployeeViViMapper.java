package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextEmployeeViVi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextEmployeeViViDTO;

/**
 * Mapper for the entity {@link NextEmployeeViVi} and its DTO {@link NextEmployeeViViDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextEmployeeViViMapper extends EntityMapper<NextEmployeeViViDTO, NextEmployeeViVi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextEmployeeViViDTO toDto(NextEmployeeViVi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
