package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextInvoiceMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextInvoiceMiDTO;

/**
 * Mapper for the entity {@link NextInvoiceMi} and its DTO {@link NextInvoiceMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextInvoiceMiMapper extends EntityMapper<NextInvoiceMiDTO, NextInvoiceMi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextInvoiceMiDTO toDto(NextInvoiceMi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
