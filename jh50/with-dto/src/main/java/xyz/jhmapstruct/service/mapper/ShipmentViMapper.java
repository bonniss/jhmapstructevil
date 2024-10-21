package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.ShipmentVi;
import xyz.jhmapstruct.service.dto.ShipmentViDTO;

/**
 * Mapper for the entity {@link ShipmentVi} and its DTO {@link ShipmentViDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShipmentViMapper extends EntityMapper<ShipmentViDTO, ShipmentVi> {}
