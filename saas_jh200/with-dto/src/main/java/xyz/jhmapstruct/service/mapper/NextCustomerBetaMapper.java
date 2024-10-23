package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCustomerBeta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCustomerBetaDTO;

/**
 * Mapper for the entity {@link NextCustomerBeta} and its DTO {@link NextCustomerBetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextCustomerBetaMapper extends EntityMapper<NextCustomerBetaDTO, NextCustomerBeta> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextCustomerBetaDTO toDto(NextCustomerBeta s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
