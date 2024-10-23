package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextShipmentGamma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextShipmentGammaDTO;

/**
 * Mapper for the entity {@link NextShipmentGamma} and its DTO {@link NextShipmentGammaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextShipmentGammaMapper extends EntityMapper<NextShipmentGammaDTO, NextShipmentGamma> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextShipmentGammaDTO toDto(NextShipmentGamma s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
