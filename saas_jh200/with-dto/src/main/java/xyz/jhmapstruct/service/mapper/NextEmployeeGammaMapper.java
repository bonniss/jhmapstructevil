package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextEmployeeGamma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextEmployeeGammaDTO;

/**
 * Mapper for the entity {@link NextEmployeeGamma} and its DTO {@link NextEmployeeGammaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextEmployeeGammaMapper extends EntityMapper<NextEmployeeGammaDTO, NextEmployeeGamma> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextEmployeeGammaDTO toDto(NextEmployeeGamma s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
