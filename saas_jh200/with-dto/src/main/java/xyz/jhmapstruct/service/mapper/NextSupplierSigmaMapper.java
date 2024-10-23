package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextProductSigma;
import xyz.jhmapstruct.domain.NextSupplierSigma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextProductSigmaDTO;
import xyz.jhmapstruct.service.dto.NextSupplierSigmaDTO;

/**
 * Mapper for the entity {@link NextSupplierSigma} and its DTO {@link NextSupplierSigmaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextSupplierSigmaMapper extends EntityMapper<NextSupplierSigmaDTO, NextSupplierSigma> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "products", source = "products", qualifiedByName = "nextProductSigmaNameSet")
    NextSupplierSigmaDTO toDto(NextSupplierSigma s);

    @Mapping(target = "removeProducts", ignore = true)
    NextSupplierSigma toEntity(NextSupplierSigmaDTO nextSupplierSigmaDTO);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextProductSigmaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextProductSigmaDTO toDtoNextProductSigmaName(NextProductSigma nextProductSigma);

    @Named("nextProductSigmaNameSet")
    default Set<NextProductSigmaDTO> toDtoNextProductSigmaNameSet(Set<NextProductSigma> nextProductSigma) {
        return nextProductSigma.stream().map(this::toDtoNextProductSigmaName).collect(Collectors.toSet());
    }
}
