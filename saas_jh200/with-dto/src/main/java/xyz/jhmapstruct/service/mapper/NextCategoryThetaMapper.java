package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCategoryTheta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCategoryThetaDTO;

/**
 * Mapper for the entity {@link NextCategoryTheta} and its DTO {@link NextCategoryThetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextCategoryThetaMapper extends EntityMapper<NextCategoryThetaDTO, NextCategoryTheta> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextCategoryThetaDTO toDto(NextCategoryTheta s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
