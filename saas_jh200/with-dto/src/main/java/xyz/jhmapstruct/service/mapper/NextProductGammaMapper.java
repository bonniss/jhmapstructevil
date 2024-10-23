package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCategoryGamma;
import xyz.jhmapstruct.domain.NextOrderGamma;
import xyz.jhmapstruct.domain.NextProductGamma;
import xyz.jhmapstruct.domain.NextSupplierGamma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCategoryGammaDTO;
import xyz.jhmapstruct.service.dto.NextOrderGammaDTO;
import xyz.jhmapstruct.service.dto.NextProductGammaDTO;
import xyz.jhmapstruct.service.dto.NextSupplierGammaDTO;

/**
 * Mapper for the entity {@link NextProductGamma} and its DTO {@link NextProductGammaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextProductGammaMapper extends EntityMapper<NextProductGammaDTO, NextProductGamma> {
    @Mapping(target = "category", source = "category", qualifiedByName = "nextCategoryGammaName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "order", source = "order", qualifiedByName = "nextOrderGammaId")
    @Mapping(target = "suppliers", source = "suppliers", qualifiedByName = "nextSupplierGammaIdSet")
    NextProductGammaDTO toDto(NextProductGamma s);

    @Mapping(target = "suppliers", ignore = true)
    @Mapping(target = "removeSuppliers", ignore = true)
    NextProductGamma toEntity(NextProductGammaDTO nextProductGammaDTO);

    @Named("nextCategoryGammaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextCategoryGammaDTO toDtoNextCategoryGammaName(NextCategoryGamma nextCategoryGamma);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextOrderGammaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextOrderGammaDTO toDtoNextOrderGammaId(NextOrderGamma nextOrderGamma);

    @Named("nextSupplierGammaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextSupplierGammaDTO toDtoNextSupplierGammaId(NextSupplierGamma nextSupplierGamma);

    @Named("nextSupplierGammaIdSet")
    default Set<NextSupplierGammaDTO> toDtoNextSupplierGammaIdSet(Set<NextSupplierGamma> nextSupplierGamma) {
        return nextSupplierGamma.stream().map(this::toDtoNextSupplierGammaId).collect(Collectors.toSet());
    }
}
