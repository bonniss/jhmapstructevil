package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextEmployeeTheta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextEmployeeThetaDTO;

/**
 * Mapper for the entity {@link NextEmployeeTheta} and its DTO {@link NextEmployeeThetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextEmployeeThetaMapper extends EntityMapper<NextEmployeeThetaDTO, NextEmployeeTheta> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextEmployeeThetaDTO toDto(NextEmployeeTheta s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
