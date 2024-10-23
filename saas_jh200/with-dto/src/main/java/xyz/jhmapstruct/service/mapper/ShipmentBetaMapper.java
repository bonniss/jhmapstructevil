package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.ShipmentBeta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.ShipmentBetaDTO;

/**
 * Mapper for the entity {@link ShipmentBeta} and its DTO {@link ShipmentBetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShipmentBetaMapper extends EntityMapper<ShipmentBetaDTO, ShipmentBeta> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    ShipmentBetaDTO toDto(ShipmentBeta s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
