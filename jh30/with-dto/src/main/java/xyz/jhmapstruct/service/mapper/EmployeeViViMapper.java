package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.EmployeeViVi;
import xyz.jhmapstruct.service.dto.EmployeeViViDTO;

/**
 * Mapper for the entity {@link EmployeeViVi} and its DTO {@link EmployeeViViDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeViViMapper extends EntityMapper<EmployeeViViDTO, EmployeeViVi> {}
