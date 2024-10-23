package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextShipmentTheta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextShipmentThetaDTO;

/**
 * Mapper for the entity {@link NextShipmentTheta} and its DTO {@link NextShipmentThetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextShipmentThetaMapper extends EntityMapper<NextShipmentThetaDTO, NextShipmentTheta> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextShipmentThetaDTO toDto(NextShipmentTheta s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
