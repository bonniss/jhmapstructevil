package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextInvoiceVi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextInvoiceViDTO;

/**
 * Mapper for the entity {@link NextInvoiceVi} and its DTO {@link NextInvoiceViDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextInvoiceViMapper extends EntityMapper<NextInvoiceViDTO, NextInvoiceVi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextInvoiceViDTO toDto(NextInvoiceVi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
