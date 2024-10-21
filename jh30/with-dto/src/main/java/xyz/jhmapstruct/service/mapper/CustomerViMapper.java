package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.CustomerVi;
import xyz.jhmapstruct.service.dto.CustomerViDTO;

/**
 * Mapper for the entity {@link CustomerVi} and its DTO {@link CustomerViDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerViMapper extends EntityMapper<CustomerViDTO, CustomerVi> {}
