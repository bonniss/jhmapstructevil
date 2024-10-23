package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextPaymentMiMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextPaymentMiMiDTO;

/**
 * Mapper for the entity {@link NextPaymentMiMi} and its DTO {@link NextPaymentMiMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextPaymentMiMiMapper extends EntityMapper<NextPaymentMiMiDTO, NextPaymentMiMi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextPaymentMiMiDTO toDto(NextPaymentMiMi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
