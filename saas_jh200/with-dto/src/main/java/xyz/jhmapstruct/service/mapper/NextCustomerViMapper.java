package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCustomerVi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCustomerViDTO;

/**
 * Mapper for the entity {@link NextCustomerVi} and its DTO {@link NextCustomerViDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextCustomerViMapper extends EntityMapper<NextCustomerViDTO, NextCustomerVi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextCustomerViDTO toDto(NextCustomerVi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
