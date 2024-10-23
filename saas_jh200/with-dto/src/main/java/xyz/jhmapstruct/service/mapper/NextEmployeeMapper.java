package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextEmployee;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextEmployeeDTO;

/**
 * Mapper for the entity {@link NextEmployee} and its DTO {@link NextEmployeeDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextEmployeeMapper extends EntityMapper<NextEmployeeDTO, NextEmployee> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextEmployeeDTO toDto(NextEmployee s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
