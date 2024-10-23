package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextShipmentSigma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextShipmentSigmaDTO;

/**
 * Mapper for the entity {@link NextShipmentSigma} and its DTO {@link NextShipmentSigmaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextShipmentSigmaMapper extends EntityMapper<NextShipmentSigmaDTO, NextShipmentSigma> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextShipmentSigmaDTO toDto(NextShipmentSigma s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
