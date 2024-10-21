package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.CustomerViVi;
import xyz.jhmapstruct.service.dto.CustomerViViDTO;

/**
 * Mapper for the entity {@link CustomerViVi} and its DTO {@link CustomerViViDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerViViMapper extends EntityMapper<CustomerViViDTO, CustomerViVi> {}
