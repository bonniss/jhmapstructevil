package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextInvoiceTheta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextInvoiceThetaDTO;

/**
 * Mapper for the entity {@link NextInvoiceTheta} and its DTO {@link NextInvoiceThetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextInvoiceThetaMapper extends EntityMapper<NextInvoiceThetaDTO, NextInvoiceTheta> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextInvoiceThetaDTO toDto(NextInvoiceTheta s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
