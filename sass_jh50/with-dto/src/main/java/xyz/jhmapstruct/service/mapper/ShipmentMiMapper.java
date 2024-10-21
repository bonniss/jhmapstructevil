package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.ShipmentMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.ShipmentMiDTO;

/**
 * Mapper for the entity {@link ShipmentMi} and its DTO {@link ShipmentMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShipmentMiMapper extends EntityMapper<ShipmentMiDTO, ShipmentMi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    ShipmentMiDTO toDto(ShipmentMi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
