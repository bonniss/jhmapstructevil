package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextInvoiceBeta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextInvoiceBetaDTO;

/**
 * Mapper for the entity {@link NextInvoiceBeta} and its DTO {@link NextInvoiceBetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextInvoiceBetaMapper extends EntityMapper<NextInvoiceBetaDTO, NextInvoiceBeta> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextInvoiceBetaDTO toDto(NextInvoiceBeta s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
