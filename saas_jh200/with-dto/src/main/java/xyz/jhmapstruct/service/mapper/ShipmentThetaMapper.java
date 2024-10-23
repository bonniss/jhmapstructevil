package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.ShipmentTheta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.ShipmentThetaDTO;

/**
 * Mapper for the entity {@link ShipmentTheta} and its DTO {@link ShipmentThetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShipmentThetaMapper extends EntityMapper<ShipmentThetaDTO, ShipmentTheta> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    ShipmentThetaDTO toDto(ShipmentTheta s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
