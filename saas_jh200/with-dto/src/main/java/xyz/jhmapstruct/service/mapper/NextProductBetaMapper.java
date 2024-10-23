package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCategoryBeta;
import xyz.jhmapstruct.domain.NextOrderBeta;
import xyz.jhmapstruct.domain.NextProductBeta;
import xyz.jhmapstruct.domain.NextSupplierBeta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCategoryBetaDTO;
import xyz.jhmapstruct.service.dto.NextOrderBetaDTO;
import xyz.jhmapstruct.service.dto.NextProductBetaDTO;
import xyz.jhmapstruct.service.dto.NextSupplierBetaDTO;

/**
 * Mapper for the entity {@link NextProductBeta} and its DTO {@link NextProductBetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextProductBetaMapper extends EntityMapper<NextProductBetaDTO, NextProductBeta> {
    @Mapping(target = "category", source = "category", qualifiedByName = "nextCategoryBetaName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "order", source = "order", qualifiedByName = "nextOrderBetaId")
    @Mapping(target = "suppliers", source = "suppliers", qualifiedByName = "nextSupplierBetaIdSet")
    NextProductBetaDTO toDto(NextProductBeta s);

    @Mapping(target = "suppliers", ignore = true)
    @Mapping(target = "removeSuppliers", ignore = true)
    NextProductBeta toEntity(NextProductBetaDTO nextProductBetaDTO);

    @Named("nextCategoryBetaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextCategoryBetaDTO toDtoNextCategoryBetaName(NextCategoryBeta nextCategoryBeta);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextOrderBetaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextOrderBetaDTO toDtoNextOrderBetaId(NextOrderBeta nextOrderBeta);

    @Named("nextSupplierBetaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextSupplierBetaDTO toDtoNextSupplierBetaId(NextSupplierBeta nextSupplierBeta);

    @Named("nextSupplierBetaIdSet")
    default Set<NextSupplierBetaDTO> toDtoNextSupplierBetaIdSet(Set<NextSupplierBeta> nextSupplierBeta) {
        return nextSupplierBeta.stream().map(this::toDtoNextSupplierBetaId).collect(Collectors.toSet());
    }
}
