package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.CustomerViVi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.service.dto.CustomerViViDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;

/**
 * Mapper for the entity {@link CustomerViVi} and its DTO {@link CustomerViViDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerViViMapper extends EntityMapper<CustomerViViDTO, CustomerViVi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    CustomerViViDTO toDto(CustomerViVi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
