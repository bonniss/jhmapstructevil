package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCustomer;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCustomerDTO;

/**
 * Mapper for the entity {@link NextCustomer} and its DTO {@link NextCustomerDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextCustomerMapper extends EntityMapper<NextCustomerDTO, NextCustomer> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextCustomerDTO toDto(NextCustomer s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
