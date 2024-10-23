package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCustomerSigma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCustomerSigmaDTO;

/**
 * Mapper for the entity {@link NextCustomerSigma} and its DTO {@link NextCustomerSigmaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextCustomerSigmaMapper extends EntityMapper<NextCustomerSigmaDTO, NextCustomerSigma> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextCustomerSigmaDTO toDto(NextCustomerSigma s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
