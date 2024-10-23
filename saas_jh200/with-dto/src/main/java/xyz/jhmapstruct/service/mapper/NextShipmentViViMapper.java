package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextShipmentViVi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextShipmentViViDTO;

/**
 * Mapper for the entity {@link NextShipmentViVi} and its DTO {@link NextShipmentViViDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextShipmentViViMapper extends EntityMapper<NextShipmentViViDTO, NextShipmentViVi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextShipmentViViDTO toDto(NextShipmentViVi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
