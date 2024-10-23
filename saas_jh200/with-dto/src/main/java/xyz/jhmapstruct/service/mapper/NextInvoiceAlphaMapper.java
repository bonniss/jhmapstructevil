package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextInvoiceAlpha;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextInvoiceAlphaDTO;

/**
 * Mapper for the entity {@link NextInvoiceAlpha} and its DTO {@link NextInvoiceAlphaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextInvoiceAlphaMapper extends EntityMapper<NextInvoiceAlphaDTO, NextInvoiceAlpha> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextInvoiceAlphaDTO toDto(NextInvoiceAlpha s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
