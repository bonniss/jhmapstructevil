package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextProductBeta;
import xyz.jhmapstruct.domain.NextReviewBeta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextProductBetaDTO;
import xyz.jhmapstruct.service.dto.NextReviewBetaDTO;

/**
 * Mapper for the entity {@link NextReviewBeta} and its DTO {@link NextReviewBetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextReviewBetaMapper extends EntityMapper<NextReviewBetaDTO, NextReviewBeta> {
    @Mapping(target = "product", source = "product", qualifiedByName = "nextProductBetaName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextReviewBetaDTO toDto(NextReviewBeta s);

    @Named("nextProductBetaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextProductBetaDTO toDtoNextProductBetaName(NextProductBeta nextProductBeta);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
