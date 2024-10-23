package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.ShipmentSigma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.ShipmentSigmaDTO;

/**
 * Mapper for the entity {@link ShipmentSigma} and its DTO {@link ShipmentSigmaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShipmentSigmaMapper extends EntityMapper<ShipmentSigmaDTO, ShipmentSigma> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    ShipmentSigmaDTO toDto(ShipmentSigma s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
