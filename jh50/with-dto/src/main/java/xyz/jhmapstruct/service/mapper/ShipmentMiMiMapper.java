package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.ShipmentMiMi;
import xyz.jhmapstruct.service.dto.ShipmentMiMiDTO;

/**
 * Mapper for the entity {@link ShipmentMiMi} and its DTO {@link ShipmentMiMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShipmentMiMiMapper extends EntityMapper<ShipmentMiMiDTO, ShipmentMiMi> {}
