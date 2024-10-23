package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextShipmentAlpha;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextShipmentAlphaDTO;

/**
 * Mapper for the entity {@link NextShipmentAlpha} and its DTO {@link NextShipmentAlphaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextShipmentAlphaMapper extends EntityMapper<NextShipmentAlphaDTO, NextShipmentAlpha> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextShipmentAlphaDTO toDto(NextShipmentAlpha s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
