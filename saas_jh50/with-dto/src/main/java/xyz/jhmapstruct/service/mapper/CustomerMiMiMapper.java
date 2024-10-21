package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.CustomerMiMi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.service.dto.CustomerMiMiDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;

/**
 * Mapper for the entity {@link CustomerMiMi} and its DTO {@link CustomerMiMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerMiMiMapper extends EntityMapper<CustomerMiMiDTO, CustomerMiMi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    CustomerMiMiDTO toDto(CustomerMiMi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
