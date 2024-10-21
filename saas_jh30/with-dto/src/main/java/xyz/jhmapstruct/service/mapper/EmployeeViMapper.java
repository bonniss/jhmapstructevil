package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.EmployeeVi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.service.dto.EmployeeViDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;

/**
 * Mapper for the entity {@link EmployeeVi} and its DTO {@link EmployeeViDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeViMapper extends EntityMapper<EmployeeViDTO, EmployeeVi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    EmployeeViDTO toDto(EmployeeVi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
