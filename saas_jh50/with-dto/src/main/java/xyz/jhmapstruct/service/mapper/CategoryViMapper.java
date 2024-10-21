package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.CategoryVi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.service.dto.CategoryViDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;

/**
 * Mapper for the entity {@link CategoryVi} and its DTO {@link CategoryViDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryViMapper extends EntityMapper<CategoryViDTO, CategoryVi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    CategoryViDTO toDto(CategoryVi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
