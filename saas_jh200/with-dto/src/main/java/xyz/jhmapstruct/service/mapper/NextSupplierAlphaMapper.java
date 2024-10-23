package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextProductAlpha;
import xyz.jhmapstruct.domain.NextSupplierAlpha;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextProductAlphaDTO;
import xyz.jhmapstruct.service.dto.NextSupplierAlphaDTO;

/**
 * Mapper for the entity {@link NextSupplierAlpha} and its DTO {@link NextSupplierAlphaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextSupplierAlphaMapper extends EntityMapper<NextSupplierAlphaDTO, NextSupplierAlpha> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "products", source = "products", qualifiedByName = "nextProductAlphaNameSet")
    NextSupplierAlphaDTO toDto(NextSupplierAlpha s);

    @Mapping(target = "removeProducts", ignore = true)
    NextSupplierAlpha toEntity(NextSupplierAlphaDTO nextSupplierAlphaDTO);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextProductAlphaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextProductAlphaDTO toDtoNextProductAlphaName(NextProductAlpha nextProductAlpha);

    @Named("nextProductAlphaNameSet")
    default Set<NextProductAlphaDTO> toDtoNextProductAlphaNameSet(Set<NextProductAlpha> nextProductAlpha) {
        return nextProductAlpha.stream().map(this::toDtoNextProductAlphaName).collect(Collectors.toSet());
    }
}
