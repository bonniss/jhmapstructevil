package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.CustomerMi;
import xyz.jhmapstruct.service.dto.CustomerMiDTO;

/**
 * Mapper for the entity {@link CustomerMi} and its DTO {@link CustomerMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerMiMapper extends EntityMapper<CustomerMiDTO, CustomerMi> {}
