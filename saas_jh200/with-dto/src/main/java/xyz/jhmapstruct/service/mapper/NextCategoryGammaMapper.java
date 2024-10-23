package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCategoryGamma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCategoryGammaDTO;

/**
 * Mapper for the entity {@link NextCategoryGamma} and its DTO {@link NextCategoryGammaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextCategoryGammaMapper extends EntityMapper<NextCategoryGammaDTO, NextCategoryGamma> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextCategoryGammaDTO toDto(NextCategoryGamma s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
