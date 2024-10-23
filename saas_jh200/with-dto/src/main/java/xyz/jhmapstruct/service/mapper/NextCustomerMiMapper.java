package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCustomerMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCustomerMiDTO;

/**
 * Mapper for the entity {@link NextCustomerMi} and its DTO {@link NextCustomerMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextCustomerMiMapper extends EntityMapper<NextCustomerMiDTO, NextCustomerMi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextCustomerMiDTO toDto(NextCustomerMi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
