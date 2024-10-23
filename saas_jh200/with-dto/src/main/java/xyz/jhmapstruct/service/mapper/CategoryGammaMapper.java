package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.CategoryGamma;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.service.dto.CategoryGammaDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;

/**
 * Mapper for the entity {@link CategoryGamma} and its DTO {@link CategoryGammaDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryGammaMapper extends EntityMapper<CategoryGammaDTO, CategoryGamma> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    CategoryGammaDTO toDto(CategoryGamma s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
