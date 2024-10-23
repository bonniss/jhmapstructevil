package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextPaymentAlpha;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextPaymentAlphaDTO;

/**
 * Mapper for the entity {@link NextPaymentAlpha} and its DTO {@link NextPaymentAlphaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextPaymentAlphaMapper extends EntityMapper<NextPaymentAlphaDTO, NextPaymentAlpha> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextPaymentAlphaDTO toDto(NextPaymentAlpha s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
