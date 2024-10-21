package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.ShipmentMiMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.ShipmentMiMiDTO;

/**
 * Mapper for the entity {@link ShipmentMiMi} and its DTO {@link ShipmentMiMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShipmentMiMiMapper extends EntityMapper<ShipmentMiMiDTO, ShipmentMiMi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    ShipmentMiMiDTO toDto(ShipmentMiMi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
