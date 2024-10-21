package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.Invoice;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.service.dto.InvoiceDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;

/**
 * Mapper for the entity {@link Invoice} and its DTO {@link InvoiceDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoiceMapper extends EntityMapper<InvoiceDTO, Invoice> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    InvoiceDTO toDto(Invoice s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
