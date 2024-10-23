package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextProductBeta;
import xyz.jhmapstruct.domain.NextSupplierBeta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextProductBetaDTO;
import xyz.jhmapstruct.service.dto.NextSupplierBetaDTO;

/**
 * Mapper for the entity {@link NextSupplierBeta} and its DTO {@link NextSupplierBetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextSupplierBetaMapper extends EntityMapper<NextSupplierBetaDTO, NextSupplierBeta> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "products", source = "products", qualifiedByName = "nextProductBetaNameSet")
    NextSupplierBetaDTO toDto(NextSupplierBeta s);

    @Mapping(target = "removeProducts", ignore = true)
    NextSupplierBeta toEntity(NextSupplierBetaDTO nextSupplierBetaDTO);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextProductBetaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextProductBetaDTO toDtoNextProductBetaName(NextProductBeta nextProductBeta);

    @Named("nextProductBetaNameSet")
    default Set<NextProductBetaDTO> toDtoNextProductBetaNameSet(Set<NextProductBeta> nextProductBeta) {
        return nextProductBeta.stream().map(this::toDtoNextProductBetaName).collect(Collectors.toSet());
    }
}
