package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.InvoiceMiMi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.service.dto.InvoiceMiMiDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;

/**
 * Mapper for the entity {@link InvoiceMiMi} and its DTO {@link InvoiceMiMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoiceMiMiMapper extends EntityMapper<InvoiceMiMiDTO, InvoiceMiMi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    InvoiceMiMiDTO toDto(InvoiceMiMi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
