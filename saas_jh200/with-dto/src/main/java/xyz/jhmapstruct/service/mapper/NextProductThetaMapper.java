package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCategoryTheta;
import xyz.jhmapstruct.domain.NextOrderTheta;
import xyz.jhmapstruct.domain.NextProductTheta;
import xyz.jhmapstruct.domain.NextSupplierTheta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCategoryThetaDTO;
import xyz.jhmapstruct.service.dto.NextOrderThetaDTO;
import xyz.jhmapstruct.service.dto.NextProductThetaDTO;
import xyz.jhmapstruct.service.dto.NextSupplierThetaDTO;

/**
 * Mapper for the entity {@link NextProductTheta} and its DTO {@link NextProductThetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextProductThetaMapper extends EntityMapper<NextProductThetaDTO, NextProductTheta> {
    @Mapping(target = "category", source = "category", qualifiedByName = "nextCategoryThetaName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "order", source = "order", qualifiedByName = "nextOrderThetaId")
    @Mapping(target = "suppliers", source = "suppliers", qualifiedByName = "nextSupplierThetaIdSet")
    NextProductThetaDTO toDto(NextProductTheta s);

    @Mapping(target = "suppliers", ignore = true)
    @Mapping(target = "removeSuppliers", ignore = true)
    NextProductTheta toEntity(NextProductThetaDTO nextProductThetaDTO);

    @Named("nextCategoryThetaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextCategoryThetaDTO toDtoNextCategoryThetaName(NextCategoryTheta nextCategoryTheta);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextOrderThetaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextOrderThetaDTO toDtoNextOrderThetaId(NextOrderTheta nextOrderTheta);

    @Named("nextSupplierThetaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextSupplierThetaDTO toDtoNextSupplierThetaId(NextSupplierTheta nextSupplierTheta);

    @Named("nextSupplierThetaIdSet")
    default Set<NextSupplierThetaDTO> toDtoNextSupplierThetaIdSet(Set<NextSupplierTheta> nextSupplierTheta) {
        return nextSupplierTheta.stream().map(this::toDtoNextSupplierThetaId).collect(Collectors.toSet());
    }
}
