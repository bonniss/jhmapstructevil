package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.EmployeeMiMi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.service.dto.EmployeeMiMiDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;

/**
 * Mapper for the entity {@link EmployeeMiMi} and its DTO {@link EmployeeMiMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeMiMiMapper extends EntityMapper<EmployeeMiMiDTO, EmployeeMiMi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    EmployeeMiMiDTO toDto(EmployeeMiMi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
