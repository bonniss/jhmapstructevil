package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextShipmentMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextShipmentMiDTO;

/**
 * Mapper for the entity {@link NextShipmentMi} and its DTO {@link NextShipmentMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextShipmentMiMapper extends EntityMapper<NextShipmentMiDTO, NextShipmentMi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextShipmentMiDTO toDto(NextShipmentMi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
