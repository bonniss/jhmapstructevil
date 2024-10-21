package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.ShipmentVi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.ShipmentViDTO;

/**
 * Mapper for the entity {@link ShipmentVi} and its DTO {@link ShipmentViDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShipmentViMapper extends EntityMapper<ShipmentViDTO, ShipmentVi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    ShipmentViDTO toDto(ShipmentVi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
