package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextEmployeeBeta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextEmployeeBetaDTO;

/**
 * Mapper for the entity {@link NextEmployeeBeta} and its DTO {@link NextEmployeeBetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextEmployeeBetaMapper extends EntityMapper<NextEmployeeBetaDTO, NextEmployeeBeta> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextEmployeeBetaDTO toDto(NextEmployeeBeta s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
