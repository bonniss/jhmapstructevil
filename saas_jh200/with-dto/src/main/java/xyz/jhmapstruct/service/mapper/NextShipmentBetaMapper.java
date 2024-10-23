package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextShipmentBeta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextShipmentBetaDTO;

/**
 * Mapper for the entity {@link NextShipmentBeta} and its DTO {@link NextShipmentBetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextShipmentBetaMapper extends EntityMapper<NextShipmentBetaDTO, NextShipmentBeta> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextShipmentBetaDTO toDto(NextShipmentBeta s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
