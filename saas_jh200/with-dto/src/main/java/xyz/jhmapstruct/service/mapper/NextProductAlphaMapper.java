package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCategoryAlpha;
import xyz.jhmapstruct.domain.NextOrderAlpha;
import xyz.jhmapstruct.domain.NextProductAlpha;
import xyz.jhmapstruct.domain.NextSupplierAlpha;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCategoryAlphaDTO;
import xyz.jhmapstruct.service.dto.NextOrderAlphaDTO;
import xyz.jhmapstruct.service.dto.NextProductAlphaDTO;
import xyz.jhmapstruct.service.dto.NextSupplierAlphaDTO;

/**
 * Mapper for the entity {@link NextProductAlpha} and its DTO {@link NextProductAlphaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextProductAlphaMapper extends EntityMapper<NextProductAlphaDTO, NextProductAlpha> {
    @Mapping(target = "category", source = "category", qualifiedByName = "nextCategoryAlphaName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "order", source = "order", qualifiedByName = "nextOrderAlphaId")
    @Mapping(target = "suppliers", source = "suppliers", qualifiedByName = "nextSupplierAlphaIdSet")
    NextProductAlphaDTO toDto(NextProductAlpha s);

    @Mapping(target = "suppliers", ignore = true)
    @Mapping(target = "removeSuppliers", ignore = true)
    NextProductAlpha toEntity(NextProductAlphaDTO nextProductAlphaDTO);

    @Named("nextCategoryAlphaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextCategoryAlphaDTO toDtoNextCategoryAlphaName(NextCategoryAlpha nextCategoryAlpha);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextOrderAlphaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextOrderAlphaDTO toDtoNextOrderAlphaId(NextOrderAlpha nextOrderAlpha);

    @Named("nextSupplierAlphaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextSupplierAlphaDTO toDtoNextSupplierAlphaId(NextSupplierAlpha nextSupplierAlpha);

    @Named("nextSupplierAlphaIdSet")
    default Set<NextSupplierAlphaDTO> toDtoNextSupplierAlphaIdSet(Set<NextSupplierAlpha> nextSupplierAlpha) {
        return nextSupplierAlpha.stream().map(this::toDtoNextSupplierAlphaId).collect(Collectors.toSet());
    }
}
