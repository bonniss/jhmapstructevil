package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCategorySigma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCategorySigmaDTO;

/**
 * Mapper for the entity {@link NextCategorySigma} and its DTO {@link NextCategorySigmaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextCategorySigmaMapper extends EntityMapper<NextCategorySigmaDTO, NextCategorySigma> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextCategorySigmaDTO toDto(NextCategorySigma s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
