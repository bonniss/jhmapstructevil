package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextProductMiMi;
import xyz.jhmapstruct.domain.NextSupplierMiMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextProductMiMiDTO;
import xyz.jhmapstruct.service.dto.NextSupplierMiMiDTO;

/**
 * Mapper for the entity {@link NextSupplierMiMi} and its DTO {@link NextSupplierMiMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextSupplierMiMiMapper extends EntityMapper<NextSupplierMiMiDTO, NextSupplierMiMi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "products", source = "products", qualifiedByName = "nextProductMiMiNameSet")
    NextSupplierMiMiDTO toDto(NextSupplierMiMi s);

    @Mapping(target = "removeProducts", ignore = true)
    NextSupplierMiMi toEntity(NextSupplierMiMiDTO nextSupplierMiMiDTO);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextProductMiMiName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextProductMiMiDTO toDtoNextProductMiMiName(NextProductMiMi nextProductMiMi);

    @Named("nextProductMiMiNameSet")
    default Set<NextProductMiMiDTO> toDtoNextProductMiMiNameSet(Set<NextProductMiMi> nextProductMiMi) {
        return nextProductMiMi.stream().map(this::toDtoNextProductMiMiName).collect(Collectors.toSet());
    }
}
