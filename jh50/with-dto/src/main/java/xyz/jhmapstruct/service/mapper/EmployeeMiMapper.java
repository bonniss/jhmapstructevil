package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.EmployeeMi;
import xyz.jhmapstruct.service.dto.EmployeeMiDTO;

/**
 * Mapper for the entity {@link EmployeeMi} and its DTO {@link EmployeeMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeMiMapper extends EntityMapper<EmployeeMiDTO, EmployeeMi> {}
