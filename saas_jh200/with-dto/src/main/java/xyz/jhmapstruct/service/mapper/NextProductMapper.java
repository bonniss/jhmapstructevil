package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCategory;
import xyz.jhmapstruct.domain.NextOrder;
import xyz.jhmapstruct.domain.NextProduct;
import xyz.jhmapstruct.domain.NextSupplier;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCategoryDTO;
import xyz.jhmapstruct.service.dto.NextOrderDTO;
import xyz.jhmapstruct.service.dto.NextProductDTO;
import xyz.jhmapstruct.service.dto.NextSupplierDTO;

/**
 * Mapper for the entity {@link NextProduct} and its DTO {@link NextProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextProductMapper extends EntityMapper<NextProductDTO, NextProduct> {
    @Mapping(target = "category", source = "category", qualifiedByName = "nextCategoryName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "order", source = "order", qualifiedByName = "nextOrderId")
    @Mapping(target = "suppliers", source = "suppliers", qualifiedByName = "nextSupplierIdSet")
    NextProductDTO toDto(NextProduct s);

    @Mapping(target = "suppliers", ignore = true)
    @Mapping(target = "removeSuppliers", ignore = true)
    NextProduct toEntity(NextProductDTO nextProductDTO);

    @Named("nextCategoryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextCategoryDTO toDtoNextCategoryName(NextCategory nextCategory);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextOrderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextOrderDTO toDtoNextOrderId(NextOrder nextOrder);

    @Named("nextSupplierId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextSupplierDTO toDtoNextSupplierId(NextSupplier nextSupplier);

    @Named("nextSupplierIdSet")
    default Set<NextSupplierDTO> toDtoNextSupplierIdSet(Set<NextSupplier> nextSupplier) {
        return nextSupplier.stream().map(this::toDtoNextSupplierId).collect(Collectors.toSet());
    }
}
