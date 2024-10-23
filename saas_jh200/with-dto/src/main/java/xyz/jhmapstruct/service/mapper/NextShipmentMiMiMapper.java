package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextShipmentMiMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextShipmentMiMiDTO;

/**
 * Mapper for the entity {@link NextShipmentMiMi} and its DTO {@link NextShipmentMiMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextShipmentMiMiMapper extends EntityMapper<NextShipmentMiMiDTO, NextShipmentMiMi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextShipmentMiMiDTO toDto(NextShipmentMiMi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
