package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextProductGamma;
import xyz.jhmapstruct.domain.NextSupplierGamma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextProductGammaDTO;
import xyz.jhmapstruct.service.dto.NextSupplierGammaDTO;

/**
 * Mapper for the entity {@link NextSupplierGamma} and its DTO {@link NextSupplierGammaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextSupplierGammaMapper extends EntityMapper<NextSupplierGammaDTO, NextSupplierGamma> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "products", source = "products", qualifiedByName = "nextProductGammaNameSet")
    NextSupplierGammaDTO toDto(NextSupplierGamma s);

    @Mapping(target = "removeProducts", ignore = true)
    NextSupplierGamma toEntity(NextSupplierGammaDTO nextSupplierGammaDTO);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextProductGammaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextProductGammaDTO toDtoNextProductGammaName(NextProductGamma nextProductGamma);

    @Named("nextProductGammaNameSet")
    default Set<NextProductGammaDTO> toDtoNextProductGammaNameSet(Set<NextProductGamma> nextProductGamma) {
        return nextProductGamma.stream().map(this::toDtoNextProductGammaName).collect(Collectors.toSet());
    }
}
