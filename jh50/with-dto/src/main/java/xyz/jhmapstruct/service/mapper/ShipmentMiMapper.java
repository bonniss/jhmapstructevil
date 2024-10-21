package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.ShipmentMi;
import xyz.jhmapstruct.service.dto.ShipmentMiDTO;

/**
 * Mapper for the entity {@link ShipmentMi} and its DTO {@link ShipmentMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShipmentMiMapper extends EntityMapper<ShipmentMiDTO, ShipmentMi> {}
