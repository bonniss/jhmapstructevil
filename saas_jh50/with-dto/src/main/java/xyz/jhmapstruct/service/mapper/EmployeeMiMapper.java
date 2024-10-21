package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.EmployeeMi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.service.dto.EmployeeMiDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;

/**
 * Mapper for the entity {@link EmployeeMi} and its DTO {@link EmployeeMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeMiMapper extends EntityMapper<EmployeeMiDTO, EmployeeMi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    EmployeeMiDTO toDto(EmployeeMi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
