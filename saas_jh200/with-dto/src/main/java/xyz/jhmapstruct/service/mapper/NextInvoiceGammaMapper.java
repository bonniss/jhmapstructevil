package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextInvoiceGamma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextInvoiceGammaDTO;

/**
 * Mapper for the entity {@link NextInvoiceGamma} and its DTO {@link NextInvoiceGammaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextInvoiceGammaMapper extends EntityMapper<NextInvoiceGammaDTO, NextInvoiceGamma> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextInvoiceGammaDTO toDto(NextInvoiceGamma s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
