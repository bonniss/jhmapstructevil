package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextPaymentMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextPaymentMiDTO;

/**
 * Mapper for the entity {@link NextPaymentMi} and its DTO {@link NextPaymentMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextPaymentMiMapper extends EntityMapper<NextPaymentMiDTO, NextPaymentMi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextPaymentMiDTO toDto(NextPaymentMi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
