package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextInvoiceViVi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextInvoiceViViDTO;

/**
 * Mapper for the entity {@link NextInvoiceViVi} and its DTO {@link NextInvoiceViViDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextInvoiceViViMapper extends EntityMapper<NextInvoiceViViDTO, NextInvoiceViVi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextInvoiceViViDTO toDto(NextInvoiceViVi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
