package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.ShipmentViVi;
import xyz.jhmapstruct.service.dto.ShipmentViViDTO;

/**
 * Mapper for the entity {@link ShipmentViVi} and its DTO {@link ShipmentViViDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShipmentViViMapper extends EntityMapper<ShipmentViViDTO, ShipmentViVi> {}
