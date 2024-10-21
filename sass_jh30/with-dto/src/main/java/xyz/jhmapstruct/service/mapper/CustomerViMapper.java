package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.CustomerVi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.service.dto.CustomerViDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;

/**
 * Mapper for the entity {@link CustomerVi} and its DTO {@link CustomerViDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerViMapper extends EntityMapper<CustomerViDTO, CustomerVi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    CustomerViDTO toDto(CustomerVi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
