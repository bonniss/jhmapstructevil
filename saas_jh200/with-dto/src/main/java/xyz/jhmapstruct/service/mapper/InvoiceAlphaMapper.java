package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.InvoiceAlpha;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.service.dto.InvoiceAlphaDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;

/**
 * Mapper for the entity {@link InvoiceAlpha} and its DTO {@link InvoiceAlphaDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoiceAlphaMapper extends EntityMapper<InvoiceAlphaDTO, InvoiceAlpha> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    InvoiceAlphaDTO toDto(InvoiceAlpha s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
