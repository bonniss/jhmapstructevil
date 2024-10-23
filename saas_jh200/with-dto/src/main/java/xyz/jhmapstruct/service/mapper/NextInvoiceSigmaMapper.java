package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextInvoiceSigma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextInvoiceSigmaDTO;

/**
 * Mapper for the entity {@link NextInvoiceSigma} and its DTO {@link NextInvoiceSigmaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextInvoiceSigmaMapper extends EntityMapper<NextInvoiceSigmaDTO, NextInvoiceSigma> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextInvoiceSigmaDTO toDto(NextInvoiceSigma s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
