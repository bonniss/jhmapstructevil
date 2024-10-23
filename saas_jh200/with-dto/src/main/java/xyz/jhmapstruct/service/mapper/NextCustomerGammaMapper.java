package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCustomerGamma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCustomerGammaDTO;

/**
 * Mapper for the entity {@link NextCustomerGamma} and its DTO {@link NextCustomerGammaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextCustomerGammaMapper extends EntityMapper<NextCustomerGammaDTO, NextCustomerGamma> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextCustomerGammaDTO toDto(NextCustomerGamma s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
