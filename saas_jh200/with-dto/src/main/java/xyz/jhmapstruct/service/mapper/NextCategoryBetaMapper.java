package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCategoryBeta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCategoryBetaDTO;

/**
 * Mapper for the entity {@link NextCategoryBeta} and its DTO {@link NextCategoryBetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextCategoryBetaMapper extends EntityMapper<NextCategoryBetaDTO, NextCategoryBeta> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextCategoryBetaDTO toDto(NextCategoryBeta s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
