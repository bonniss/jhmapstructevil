package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.Customer;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.service.dto.CustomerDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    CustomerDTO toDto(Customer s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
