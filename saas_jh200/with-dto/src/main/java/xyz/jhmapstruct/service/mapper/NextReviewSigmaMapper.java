package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextProductSigma;
import xyz.jhmapstruct.domain.NextReviewSigma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextProductSigmaDTO;
import xyz.jhmapstruct.service.dto.NextReviewSigmaDTO;

/**
 * Mapper for the entity {@link NextReviewSigma} and its DTO {@link NextReviewSigmaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextReviewSigmaMapper extends EntityMapper<NextReviewSigmaDTO, NextReviewSigma> {
    @Mapping(target = "product", source = "product", qualifiedByName = "nextProductSigmaName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextReviewSigmaDTO toDto(NextReviewSigma s);

    @Named("nextProductSigmaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextProductSigmaDTO toDtoNextProductSigmaName(NextProductSigma nextProductSigma);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
