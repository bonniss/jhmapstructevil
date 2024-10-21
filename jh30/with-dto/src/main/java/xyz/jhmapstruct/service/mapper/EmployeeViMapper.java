package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.EmployeeVi;
import xyz.jhmapstruct.service.dto.EmployeeViDTO;

/**
 * Mapper for the entity {@link EmployeeVi} and its DTO {@link EmployeeViDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeViMapper extends EntityMapper<EmployeeViDTO, EmployeeVi> {}
