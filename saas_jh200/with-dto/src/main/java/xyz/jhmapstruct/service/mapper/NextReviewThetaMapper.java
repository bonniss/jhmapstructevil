package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextProductTheta;
import xyz.jhmapstruct.domain.NextReviewTheta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextProductThetaDTO;
import xyz.jhmapstruct.service.dto.NextReviewThetaDTO;

/**
 * Mapper for the entity {@link NextReviewTheta} and its DTO {@link NextReviewThetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextReviewThetaMapper extends EntityMapper<NextReviewThetaDTO, NextReviewTheta> {
    @Mapping(target = "product", source = "product", qualifiedByName = "nextProductThetaName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextReviewThetaDTO toDto(NextReviewTheta s);

    @Named("nextProductThetaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextProductThetaDTO toDtoNextProductThetaName(NextProductTheta nextProductTheta);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
