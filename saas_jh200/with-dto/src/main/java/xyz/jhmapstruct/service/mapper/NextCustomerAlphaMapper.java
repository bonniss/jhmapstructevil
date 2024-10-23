package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCustomerAlpha;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCustomerAlphaDTO;

/**
 * Mapper for the entity {@link NextCustomerAlpha} and its DTO {@link NextCustomerAlphaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextCustomerAlphaMapper extends EntityMapper<NextCustomerAlphaDTO, NextCustomerAlpha> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextCustomerAlphaDTO toDto(NextCustomerAlpha s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
