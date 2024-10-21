package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.CustomerMi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.service.dto.CustomerMiDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;

/**
 * Mapper for the entity {@link CustomerMi} and its DTO {@link CustomerMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerMiMapper extends EntityMapper<CustomerMiDTO, CustomerMi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    CustomerMiDTO toDto(CustomerMi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
