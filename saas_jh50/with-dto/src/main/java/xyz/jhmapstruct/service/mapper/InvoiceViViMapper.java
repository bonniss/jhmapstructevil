package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.InvoiceViVi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.service.dto.InvoiceViViDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;

/**
 * Mapper for the entity {@link InvoiceViVi} and its DTO {@link InvoiceViViDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoiceViViMapper extends EntityMapper<InvoiceViViDTO, InvoiceViVi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    InvoiceViViDTO toDto(InvoiceViVi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
