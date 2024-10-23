package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextProductMi;
import xyz.jhmapstruct.domain.SupplierMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextProductMiDTO;
import xyz.jhmapstruct.service.dto.SupplierMiDTO;

/**
 * Mapper for the entity {@link SupplierMi} and its DTO {@link SupplierMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface SupplierMiMapper extends EntityMapper<SupplierMiDTO, SupplierMi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "products", source = "products", qualifiedByName = "nextProductMiNameSet")
    SupplierMiDTO toDto(SupplierMi s);

    @Mapping(target = "removeProducts", ignore = true)
    SupplierMi toEntity(SupplierMiDTO supplierMiDTO);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextProductMiName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextProductMiDTO toDtoNextProductMiName(NextProductMi nextProductMi);

    @Named("nextProductMiNameSet")
    default Set<NextProductMiDTO> toDtoNextProductMiNameSet(Set<NextProductMi> nextProductMi) {
        return nextProductMi.stream().map(this::toDtoNextProductMiName).collect(Collectors.toSet());
    }
}
