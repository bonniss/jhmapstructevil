package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextProductTheta;
import xyz.jhmapstruct.domain.NextSupplierTheta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextProductThetaDTO;
import xyz.jhmapstruct.service.dto.NextSupplierThetaDTO;

/**
 * Mapper for the entity {@link NextSupplierTheta} and its DTO {@link NextSupplierThetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextSupplierThetaMapper extends EntityMapper<NextSupplierThetaDTO, NextSupplierTheta> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "products", source = "products", qualifiedByName = "nextProductThetaNameSet")
    NextSupplierThetaDTO toDto(NextSupplierTheta s);

    @Mapping(target = "removeProducts", ignore = true)
    NextSupplierTheta toEntity(NextSupplierThetaDTO nextSupplierThetaDTO);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextProductThetaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextProductThetaDTO toDtoNextProductThetaName(NextProductTheta nextProductTheta);

    @Named("nextProductThetaNameSet")
    default Set<NextProductThetaDTO> toDtoNextProductThetaNameSet(Set<NextProductTheta> nextProductTheta) {
        return nextProductTheta.stream().map(this::toDtoNextProductThetaName).collect(Collectors.toSet());
    }
}
