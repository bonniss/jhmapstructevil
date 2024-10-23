package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.ShipmentAlpha;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.ShipmentAlphaDTO;

/**
 * Mapper for the entity {@link ShipmentAlpha} and its DTO {@link ShipmentAlphaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShipmentAlphaMapper extends EntityMapper<ShipmentAlphaDTO, ShipmentAlpha> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    ShipmentAlphaDTO toDto(ShipmentAlpha s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
