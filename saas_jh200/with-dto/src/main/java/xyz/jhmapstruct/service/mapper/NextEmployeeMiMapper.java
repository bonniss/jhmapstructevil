package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextEmployeeMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextEmployeeMiDTO;

/**
 * Mapper for the entity {@link NextEmployeeMi} and its DTO {@link NextEmployeeMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextEmployeeMiMapper extends EntityMapper<NextEmployeeMiDTO, NextEmployeeMi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextEmployeeMiDTO toDto(NextEmployeeMi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
