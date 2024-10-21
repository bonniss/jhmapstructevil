package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.CategoryMiMi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.service.dto.CategoryMiMiDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;

/**
 * Mapper for the entity {@link CategoryMiMi} and its DTO {@link CategoryMiMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryMiMiMapper extends EntityMapper<CategoryMiMiDTO, CategoryMiMi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    CategoryMiMiDTO toDto(CategoryMiMi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
