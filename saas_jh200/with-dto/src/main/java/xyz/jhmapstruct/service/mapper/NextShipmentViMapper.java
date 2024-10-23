package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextShipmentVi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextShipmentViDTO;

/**
 * Mapper for the entity {@link NextShipmentVi} and its DTO {@link NextShipmentViDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextShipmentViMapper extends EntityMapper<NextShipmentViDTO, NextShipmentVi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextShipmentViDTO toDto(NextShipmentVi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
