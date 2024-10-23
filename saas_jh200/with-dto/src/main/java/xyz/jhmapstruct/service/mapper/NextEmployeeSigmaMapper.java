package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextEmployeeSigma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextEmployeeSigmaDTO;

/**
 * Mapper for the entity {@link NextEmployeeSigma} and its DTO {@link NextEmployeeSigmaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextEmployeeSigmaMapper extends EntityMapper<NextEmployeeSigmaDTO, NextEmployeeSigma> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextEmployeeSigmaDTO toDto(NextEmployeeSigma s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
