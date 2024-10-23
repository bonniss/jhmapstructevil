package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextProduct;
import xyz.jhmapstruct.domain.NextSupplier;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextProductDTO;
import xyz.jhmapstruct.service.dto.NextSupplierDTO;

/**
 * Mapper for the entity {@link NextSupplier} and its DTO {@link NextSupplierDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextSupplierMapper extends EntityMapper<NextSupplierDTO, NextSupplier> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "products", source = "products", qualifiedByName = "nextProductNameSet")
    NextSupplierDTO toDto(NextSupplier s);

    @Mapping(target = "removeProducts", ignore = true)
    NextSupplier toEntity(NextSupplierDTO nextSupplierDTO);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextProductName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextProductDTO toDtoNextProductName(NextProduct nextProduct);

    @Named("nextProductNameSet")
    default Set<NextProductDTO> toDtoNextProductNameSet(Set<NextProduct> nextProduct) {
        return nextProduct.stream().map(this::toDtoNextProductName).collect(Collectors.toSet());
    }
}
