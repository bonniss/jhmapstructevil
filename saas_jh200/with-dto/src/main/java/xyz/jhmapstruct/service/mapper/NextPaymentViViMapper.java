package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextPaymentViVi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextPaymentViViDTO;

/**
 * Mapper for the entity {@link NextPaymentViVi} and its DTO {@link NextPaymentViViDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextPaymentViViMapper extends EntityMapper<NextPaymentViViDTO, NextPaymentViVi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextPaymentViViDTO toDto(NextPaymentViVi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
