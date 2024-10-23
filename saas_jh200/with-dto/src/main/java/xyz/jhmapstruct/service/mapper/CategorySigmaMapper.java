package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.CategorySigma;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.service.dto.CategorySigmaDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;

/**
 * Mapper for the entity {@link CategorySigma} and its DTO {@link CategorySigmaDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategorySigmaMapper extends EntityMapper<CategorySigmaDTO, CategorySigma> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    CategorySigmaDTO toDto(CategorySigma s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
