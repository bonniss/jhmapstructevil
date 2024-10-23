package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextProductVi;
import xyz.jhmapstruct.domain.NextSupplierVi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextProductViDTO;
import xyz.jhmapstruct.service.dto.NextSupplierViDTO;

/**
 * Mapper for the entity {@link NextSupplierVi} and its DTO {@link NextSupplierViDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextSupplierViMapper extends EntityMapper<NextSupplierViDTO, NextSupplierVi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "products", source = "products", qualifiedByName = "nextProductViNameSet")
    NextSupplierViDTO toDto(NextSupplierVi s);

    @Mapping(target = "removeProducts", ignore = true)
    NextSupplierVi toEntity(NextSupplierViDTO nextSupplierViDTO);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextProductViName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextProductViDTO toDtoNextProductViName(NextProductVi nextProductVi);

    @Named("nextProductViNameSet")
    default Set<NextProductViDTO> toDtoNextProductViNameSet(Set<NextProductVi> nextProductVi) {
        return nextProductVi.stream().map(this::toDtoNextProductViName).collect(Collectors.toSet());
    }
}
