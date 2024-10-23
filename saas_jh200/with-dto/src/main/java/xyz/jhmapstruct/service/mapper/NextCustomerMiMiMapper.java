package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCustomerMiMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCustomerMiMiDTO;

/**
 * Mapper for the entity {@link NextCustomerMiMi} and its DTO {@link NextCustomerMiMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextCustomerMiMiMapper extends EntityMapper<NextCustomerMiMiDTO, NextCustomerMiMi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextCustomerMiMiDTO toDto(NextCustomerMiMi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
