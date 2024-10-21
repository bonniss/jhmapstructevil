package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.EmployeeViVi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.service.dto.EmployeeViViDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;

/**
 * Mapper for the entity {@link EmployeeViVi} and its DTO {@link EmployeeViViDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeViViMapper extends EntityMapper<EmployeeViViDTO, EmployeeViVi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    EmployeeViViDTO toDto(EmployeeViVi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
