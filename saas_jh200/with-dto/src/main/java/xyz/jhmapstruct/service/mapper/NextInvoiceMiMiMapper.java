package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextInvoiceMiMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextInvoiceMiMiDTO;

/**
 * Mapper for the entity {@link NextInvoiceMiMi} and its DTO {@link NextInvoiceMiMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextInvoiceMiMiMapper extends EntityMapper<NextInvoiceMiMiDTO, NextInvoiceMiMi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextInvoiceMiMiDTO toDto(NextInvoiceMiMi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
