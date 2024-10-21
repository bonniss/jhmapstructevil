package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.ShipmentViVi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.ShipmentViViDTO;

/**
 * Mapper for the entity {@link ShipmentViVi} and its DTO {@link ShipmentViViDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShipmentViViMapper extends EntityMapper<ShipmentViViDTO, ShipmentViVi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    ShipmentViViDTO toDto(ShipmentViVi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
