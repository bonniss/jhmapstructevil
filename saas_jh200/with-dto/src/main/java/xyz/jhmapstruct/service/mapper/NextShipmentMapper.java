package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextShipment;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextShipmentDTO;

/**
 * Mapper for the entity {@link NextShipment} and its DTO {@link NextShipmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextShipmentMapper extends EntityMapper<NextShipmentDTO, NextShipment> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextShipmentDTO toDto(NextShipment s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
