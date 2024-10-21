package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.InvoiceVi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.service.dto.InvoiceViDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;

/**
 * Mapper for the entity {@link InvoiceVi} and its DTO {@link InvoiceViDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoiceViMapper extends EntityMapper<InvoiceViDTO, InvoiceVi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    InvoiceViDTO toDto(InvoiceVi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
