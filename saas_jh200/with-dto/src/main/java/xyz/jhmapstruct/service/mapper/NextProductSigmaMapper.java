package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCategorySigma;
import xyz.jhmapstruct.domain.NextOrderSigma;
import xyz.jhmapstruct.domain.NextProductSigma;
import xyz.jhmapstruct.domain.NextSupplierSigma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCategorySigmaDTO;
import xyz.jhmapstruct.service.dto.NextOrderSigmaDTO;
import xyz.jhmapstruct.service.dto.NextProductSigmaDTO;
import xyz.jhmapstruct.service.dto.NextSupplierSigmaDTO;

/**
 * Mapper for the entity {@link NextProductSigma} and its DTO {@link NextProductSigmaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextProductSigmaMapper extends EntityMapper<NextProductSigmaDTO, NextProductSigma> {
    @Mapping(target = "category", source = "category", qualifiedByName = "nextCategorySigmaName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "order", source = "order", qualifiedByName = "nextOrderSigmaId")
    @Mapping(target = "suppliers", source = "suppliers", qualifiedByName = "nextSupplierSigmaIdSet")
    NextProductSigmaDTO toDto(NextProductSigma s);

    @Mapping(target = "suppliers", ignore = true)
    @Mapping(target = "removeSuppliers", ignore = true)
    NextProductSigma toEntity(NextProductSigmaDTO nextProductSigmaDTO);

    @Named("nextCategorySigmaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextCategorySigmaDTO toDtoNextCategorySigmaName(NextCategorySigma nextCategorySigma);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextOrderSigmaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextOrderSigmaDTO toDtoNextOrderSigmaId(NextOrderSigma nextOrderSigma);

    @Named("nextSupplierSigmaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextSupplierSigmaDTO toDtoNextSupplierSigmaId(NextSupplierSigma nextSupplierSigma);

    @Named("nextSupplierSigmaIdSet")
    default Set<NextSupplierSigmaDTO> toDtoNextSupplierSigmaIdSet(Set<NextSupplierSigma> nextSupplierSigma) {
        return nextSupplierSigma.stream().map(this::toDtoNextSupplierSigmaId).collect(Collectors.toSet());
    }
}
