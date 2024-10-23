package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;

/**
 * Mapper for the entity {@link MasterTenant} and its DTO {@link MasterTenantDTO}.
 */
@Mapper(componentModel = "spring")
public interface MasterTenantMapper extends EntityMapper<MasterTenantDTO, MasterTenant> {}
