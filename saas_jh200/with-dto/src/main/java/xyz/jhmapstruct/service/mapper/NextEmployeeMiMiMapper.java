package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextEmployeeMiMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextEmployeeMiMiDTO;

/**
 * Mapper for the entity {@link NextEmployeeMiMi} and its DTO {@link NextEmployeeMiMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextEmployeeMiMiMapper extends EntityMapper<NextEmployeeMiMiDTO, NextEmployeeMiMi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextEmployeeMiMiDTO toDto(NextEmployeeMiMi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
