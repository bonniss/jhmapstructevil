package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextInvoice;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextInvoiceDTO;

/**
 * Mapper for the entity {@link NextInvoice} and its DTO {@link NextInvoiceDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextInvoiceMapper extends EntityMapper<NextInvoiceDTO, NextInvoice> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextInvoiceDTO toDto(NextInvoice s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
