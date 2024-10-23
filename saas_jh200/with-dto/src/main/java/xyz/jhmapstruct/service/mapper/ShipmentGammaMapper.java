package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.ShipmentGamma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.ShipmentGammaDTO;

/**
 * Mapper for the entity {@link ShipmentGamma} and its DTO {@link ShipmentGammaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShipmentGammaMapper extends EntityMapper<ShipmentGammaDTO, ShipmentGamma> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    ShipmentGammaDTO toDto(ShipmentGamma s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
