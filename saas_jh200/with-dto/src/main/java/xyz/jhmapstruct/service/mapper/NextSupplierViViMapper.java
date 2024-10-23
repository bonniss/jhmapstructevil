package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextProductViVi;
import xyz.jhmapstruct.domain.NextSupplierViVi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextProductViViDTO;
import xyz.jhmapstruct.service.dto.NextSupplierViViDTO;

/**
 * Mapper for the entity {@link NextSupplierViVi} and its DTO {@link NextSupplierViViDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextSupplierViViMapper extends EntityMapper<NextSupplierViViDTO, NextSupplierViVi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "products", source = "products", qualifiedByName = "nextProductViViNameSet")
    NextSupplierViViDTO toDto(NextSupplierViVi s);

    @Mapping(target = "removeProducts", ignore = true)
    NextSupplierViVi toEntity(NextSupplierViViDTO nextSupplierViViDTO);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextProductViViName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextProductViViDTO toDtoNextProductViViName(NextProductViVi nextProductViVi);

    @Named("nextProductViViNameSet")
    default Set<NextProductViViDTO> toDtoNextProductViViNameSet(Set<NextProductViVi> nextProductViVi) {
        return nextProductViVi.stream().map(this::toDtoNextProductViViName).collect(Collectors.toSet());
    }
}
