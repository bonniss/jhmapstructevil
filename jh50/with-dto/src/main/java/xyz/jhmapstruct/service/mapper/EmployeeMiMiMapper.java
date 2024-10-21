package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.EmployeeMiMi;
import xyz.jhmapstruct.service.dto.EmployeeMiMiDTO;

/**
 * Mapper for the entity {@link EmployeeMiMi} and its DTO {@link EmployeeMiMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeMiMiMapper extends EntityMapper<EmployeeMiMiDTO, EmployeeMiMi> {}
