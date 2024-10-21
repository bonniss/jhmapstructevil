package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.CustomerMiMi;
import xyz.jhmapstruct.service.dto.CustomerMiMiDTO;

/**
 * Mapper for the entity {@link CustomerMiMi} and its DTO {@link CustomerMiMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerMiMiMapper extends EntityMapper<CustomerMiMiDTO, CustomerMiMi> {}
